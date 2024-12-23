package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.*;
import com.spring.websellspringmvc.dto.request.OrderStatusChangeRequest;
import com.spring.websellspringmvc.dto.request.datatable.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.*;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.mapper.OrderMapper;
import com.spring.websellspringmvc.models.Color;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.Size;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.services.mail.MailVerifyOrderServices;
import com.spring.websellspringmvc.services.order.OrderServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.StatusUtil;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderServicesImpl implements AdminOrderServices {
    OrderDAO orderDAO;
    OrderStatusDAO orderStatusDao;
    TransactionStatusDAO transactionStatusDao;
    CloudinaryUploadServices cloudinaryUploadServices;
    DatatableDAO datatableDAO;
    OrderServices orderServices;
    SizeDAO sizeDAO;
    ColorDAO colorDAO;
    OrderMapper mapper;
    private final Jdbi jdbi;
    private final SessionManager sessionManager;

    @Override
    public DatatableResponse<OrderDatatable> datatable(OrderDatatableRequest request) {
        List<OrderDatatable> products = datatableDAO.datatable(request);
        long total = datatableDAO.count(request);
        return DatatableResponse.<OrderDatatable>builder()
                .data(products)
                .recordsTotal(total)
                .recordsFiltered(total)
                .draw(request.getDraw())
                .build();
    }

    @Override
    public AdminOrderDetailResponse getOrderDetail(String orderId) {
        AdminOrderDetailResponse order = orderDAO.getOrder(orderId);
        List<OrderDetailItemResponse> items = orderDAO.getOrderDetailsByOrderId(orderId).stream().peek(
                orderItem -> orderItem.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), orderItem.getThumbnail()))).toList();
        order.setItems(items);
        return order;
    }

    @Override
    public List<AdminOrderDetailResponse> getOrderPrevious(String orderId) {
        // Không trả về order item
        return orderDAO.getOrderPrevious(orderId);
    }

    @Override
    public List<com.spring.websellspringmvc.models.OrderStatus> getListAllOrderStatus() {
        return orderStatusDao.getListAllOrderStatus();
    }

    @Override
    public List<com.spring.websellspringmvc.models.TransactionStatus> getListAllTransactionStatus() {
        return transactionStatusDao.getListAllTransactionStatus();
    }


    @Override
    public List<OrderStatus> getOrderStatusCanChangeByOrderId(String orderId) {
        OrderStatus orderStatus = orderStatusDao.getOrderStatus(orderId);
        if (orderStatus == null) return null;
        List<OrderStatus> orderStatusCanChange = new ArrayList<>();
        switch (orderStatus) {
            case PENDING:
                orderStatusCanChange.add(OrderStatus.PENDING);
                orderStatusCanChange.add(OrderStatus.PACKAGE);
                orderStatusCanChange.add(OrderStatus.CANCELLED);
                break;
            case PACKAGE:
                orderStatusCanChange.add(OrderStatus.PACKAGE);
                orderStatusCanChange.add(OrderStatus.DELIVERY);
                break;
            case DELIVERY:
                orderStatusCanChange.add(OrderStatus.DELIVERY);
                orderStatusCanChange.add(OrderStatus.CANCELLED);
                orderStatusCanChange.add(OrderStatus.COMPLETED);
                break;
        }
        return orderStatusCanChange;
    }


    @Override
    public List<TransactionStatus> getTransactionStatusCanChangeByOrderId(String orderId) {
        TransactionStatus transactionStatus = transactionStatusDao.getTransactionStatus(orderId);
        if (transactionStatus == null) return null;
        List<TransactionStatus> transactionStatusCanChange = new ArrayList<>();
        switch (transactionStatus) {
            case UN_PAID, PROCESSING:
                transactionStatusCanChange.add(TransactionStatus.UN_PAID);
                transactionStatusCanChange.add(TransactionStatus.PROCESSING);
                transactionStatusCanChange.add(TransactionStatus.PAID);
                break;
        }
        return transactionStatusCanChange;
    }


    @Override
    public boolean changeStatus(String orderId, OrderStatusChangeRequest request) {
        Order order = orderDAO.getOrderById(orderId);

        if (order == null) return false;
        boolean handleSendMail = jdbi.inTransaction(handle -> {
            try {
                TransactionStatus transactionStatusSrc = TransactionStatus.getByValue(order.getTransactionStatusId());
                OrderStatus orderStatusSrc = OrderStatus.getByValue(order.getOrderStatusId());

                if (request.getOrderStatus() != null && orderStatusSrc != null) {
                    if (canUpdateStatusByOrderId(orderStatusSrc, request.getOrderStatus())) {
                        orderDAO.updateOrderStatusByOrderId(orderId, request.getOrderStatus().getValue());
                    }
                }

                if (request.getTransactionStatus() != null && transactionStatusSrc != null) {
                    if (canUpdateTransactionByOrderId(transactionStatusSrc, request.getTransactionStatus())) {
                        orderDAO.updateTransactionStatusByOrderId(orderId, request.getTransactionStatus().getValue());
                    }
                }

                //        Update order detail
                if (request.getItems() != null) {
                    int rowEffect = 0;
                    for (OrderStatusChangeRequest.OrderItemChangeRequest item : request.getItems()) {
                        rowEffect += orderDAO.updateOrderDetail(item);
                    }
                    if (rowEffect != 0) {
                        orderDAO.updateOrderStatusByOrderId(orderId, OrderStatus.CHANGED.getValue());
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });

//        if (canUpdateStatusByOrderId(orderStatusSrc, request.getOrderStatus())
//                && canUpdateTransactionByOrderId(transactionStatusSrc, request.getTransactionStatus())) {
//            orderDAO.updateStatusByOrderId(orderId, request.getOrderStatus().getValue(), request.getTransactionStatus().getValue());
//            return true;
//        }

//        Gửi email thông báo
        if (handleSendMail) {
            MailVerifyOrderServices mail = new MailVerifyOrderServices(order.getEmail(), orderId);
            try {
                mail.send();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        return handleSendMail;
    }

    @Override
    public List<OrderDetailItemResponse> getOrderDetailItems(String orderId) {
        return orderServices.getOrderDetailByOrderId(orderId);
    }

    @Override
    public StatusChangedResponse getStatusCanChanged(String orderId) {
        List<OrderStatus> orderStatusList = this.getOrderStatusCanChangeByOrderId(orderId);
        List<TransactionStatus> transactionStatusList = this.getTransactionStatusCanChangeByOrderId(orderId);
        List<OrderDetailItemChangedResponse> items = null;
        List<OrderDetailItemResponse> orderItems = orderDAO.getOrderDetailsByOrderId(orderId);
        if (orderCanChangedOrderItems(orderStatusDao.getOrderStatus(orderId))) {
            items = this.getOrderDetailItem(orderItems);
        }
        return StatusChangedResponse.builder()
                .orderStatusTarget(orderStatusList)
                .transactionStatusTarget(transactionStatusList)
                .items(items)
                .build();
    }


    List<OrderDetailItemChangedResponse> getOrderDetailItem(List<OrderDetailItemResponse> orderDetailItem) {
        List<OrderDetailItemChangedResponse> response = new ArrayList<>();
        List<Integer> productId = orderDetailItem.stream().map(OrderDetailItemResponse::getProductId).toList();
        List<Size> sizes = sizeDAO.getListSizeByProductId(productId);
        List<Color> colors = colorDAO.getListColorByProductId(productId);
        for (OrderDetailItemResponse item : orderDetailItem) {
            OrderDetailItemChangedResponse itemNew = mapper.toOrderDetailItemChangedResponse(item);
            itemNew.setSizes(sizes.stream().filter(size -> size.getProductId() == item.getProductId()).toList());
            itemNew.setColors(colors.stream().filter(color -> color.getProductId() == item.getProductId()).toList());
            response.add(itemNew);
        }
        return response;
    }

    private boolean orderCanChangedOrderItems(OrderStatus orderStatus) {
        return StatusUtil.STATUS_CAN_CHANGED_ORDER_ITEMS.contains(orderStatus);
    }


    private boolean canUpdateStatusByOrderId(OrderStatus src, OrderStatus target) {
        List<OrderStatus> orderStatusCanChange = new ArrayList<>();
        switch (src) {
            case PENDING:
                orderStatusCanChange.add(OrderStatus.PENDING);
                orderStatusCanChange.add(OrderStatus.PACKAGE);
                orderStatusCanChange.add(OrderStatus.CANCELLED);
                break;
            case PACKAGE:
                orderStatusCanChange.add(OrderStatus.DELIVERY);
                orderStatusCanChange.add(OrderStatus.PACKAGE);
                break;
            case DELIVERY:
                orderStatusCanChange.add(OrderStatus.DELIVERY);
                orderStatusCanChange.add(OrderStatus.CANCELLED);
                orderStatusCanChange.add(OrderStatus.COMPLETED);
                break;
        }
        return orderStatusCanChange.contains(target);
    }

    private boolean canUpdateTransactionByOrderId(TransactionStatus src, TransactionStatus target) {
        List<TransactionStatus> transactionStatusCanChange = new ArrayList<>();
        switch (src) {
            case UN_PAID, PROCESSING:
                transactionStatusCanChange.add(TransactionStatus.UN_PAID);
                transactionStatusCanChange.add(TransactionStatus.PROCESSING);
                transactionStatusCanChange.add(TransactionStatus.PAID);
                break;
        }
        return transactionStatusCanChange.contains(target);
    }
}
