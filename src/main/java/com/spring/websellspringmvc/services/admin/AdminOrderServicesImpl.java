package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.DatatableDAO;
import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dao.OrderStatusDAO;
import com.spring.websellspringmvc.dao.TransactionStatusDAO;
import com.spring.websellspringmvc.dto.request.OrderStatusChangeRequest;
import com.spring.websellspringmvc.dto.request.datatable.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    OrderStatusDAO orderStatusDAO;
    TransactionStatusDAO transactionStatusDAO;

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
        OrderStatus orderStatus = orderStatusDAO.getOrderStatus(orderId);
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
        TransactionStatus transactionStatus = transactionStatusDAO.getTransactionStatus(orderId);
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
        Map<String, String> status = orderDAO.getStatusById(orderId);

        if (status == null) return false;

        TransactionStatus transactionStatusSrc = TransactionStatus.getByValue(Integer.parseInt(status.get("transactionStatus")));
        OrderStatus orderStatusSrc = OrderStatus.getByValue(Integer.parseInt(status.get("orderStatus")));

        if (canUpdateStatusByOrderId(orderStatusSrc, request.getOrderStatus())
                && canUpdateTransactionByOrderId(transactionStatusSrc, request.getTransactionStatus())) {
            orderDAO.updateStatusByOrderId(orderId, request.getOrderStatus().getValue(), request.getTransactionStatus().getValue());
            return true;
        }
        return false;
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
