package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.*;
import com.spring.websellspringmvc.dto.request.datatable.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.AdminOrderServices;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import com.spring.websellspringmvc.utils.FormatCurrency;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderServicesImpl implements AdminOrderServices {
    OrderDAO orderDAO;
    OrderStatusDAO orderStatusDao;
    TransactionStatusDAO transactionStatusDao;
    OrderDetailDAO orderDetailDAO;
    CloudinaryUploadServices cloudinaryUploadServices;
    DatatableDAO datatableDAO;

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
        List<OrderDetailItemResponse> items = orderDAO.getOrderDetailsByOrderId(orderId).stream().peek(
                orderItem -> orderItem.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), orderItem.getThumbnail()))).toList();
        AdminOrderDetailResponse order = orderDAO.getOrder(orderId);
        order.setItems(items);
        return order;
    }

    @Override
    public List<com.spring.websellspringmvc.models.OrderStatus> getListAllOrderStatus() {
        return orderStatusDao.getListAllOrderStatus();
    }

    @Override
    public List<com.spring.websellspringmvc.models.TransactionStatus> getListAllTransactionStatus() {
        return transactionStatusDao.getListAllTransactionStatus();
    }

    public com.spring.websellspringmvc.models.OrderStatus getOrderStatusById(int orderStatusId) {
        return orderStatusDao.getOrderStatusById(orderStatusId);
    }

    public com.spring.websellspringmvc.models.TransactionStatus getTransactionStatusById(int transactionStatusId) {
        return transactionStatusDao.getTransactionStatusById(transactionStatusId);
    }

    public List<Order> getListAllOrders() {
        return orderDAO.getListAllOrders();
    }

    public List<Order> getListOrdersBySearchFilter(String[] paymentMethod, String[] orderStatus, String[] transactionStatus, String contentSearch, String searchSelect, String startDate, String endDate) {
        return orderDAO.getListOrdersBySearchFilter(paymentMethod, orderStatus, transactionStatus, contentSearch, searchSelect, startDate, endDate);
    }
    @Override
    public List<PaymentMethod> getListAllPaymentMethodManage() {
        return orderDAO.getListAllPaymentMethodManage();
    }

    public List<DeliveryMethod> getListAllDeliveryMethodManage() {
        return orderDAO.getListAllDeliveryMethodManage();
    }

    public PaymentMethod getPaymentMethodMangeById(int id) {
        return orderDAO.getPaymentMethodMangeById(id);
    }

    public DeliveryMethod getDeliveryMethodManageById(int id) {
        return orderDAO.getDeliveryMethodManageById(id);
    }

//    public List<Order> getListOrderById(String orderId){
//        return orderDao.getListOrderByPartialId(orderId);
//    }

//    public List<Order> getListOrderByCustomerName(String customerName){
//        return orderDao.getListOrderByCustomerName(customerName);
//    }

    public Order getOrderById(String id) {
        return orderDAO.getOrderById(id);
    }

//    public void updateOrderStatusIdByOrderId(int orderStatusId , String orderId){
//        orderDao.updateOrderStatusIdByOrderId(orderStatusId, orderId);
//    }
//
//    public void updateTransactionStatusIdByOrderId(int transactionStatusId , String orderId){
//        orderDao.updateTransactionStatusIdByOrderId(transactionStatusId,orderId);
//    }

    public void removeOrderByMultipleOrderId(String[] multipleOrderId) {
        orderDetailDAO.removeOrderDetailByMultipleOrderId(multipleOrderId);
        orderDAO.removeOrderByMultipleId(multipleOrderId);
    }

    public void cancelOrderByMultipleId(String[] multipleId) {
        orderDAO.cancelOrderByArrayMultipleId(multipleId);
    }

    public List<OrderDetail> getListOrderDetailByOrderId(String orderId) {
        return orderDetailDAO.getListOrderDetailByOrderId(orderId);
    }

    public boolean updateOrder(String orderId, Integer orderStatusId, Integer transactionStatusId) {
        Order order = orderDAO.getOrderById(orderId);
        if (orderStatusId != null && transactionStatusId != null) {
            return updateStatusAndOrderTransaction(order, orderStatusId, transactionStatusId);
        }
        if (orderStatusId != null) {
            return updateOrderStatus(order, orderStatusId);
        }
        if (transactionStatusId != null) {
            return updateOrderTransaction(order, transactionStatusId);
        }
        return false;
    }

    private boolean updateStatusAndOrderTransaction(Order order, Integer orderStatusId, Integer transactionStatusId) {
        if (canUpdateStatusByOrderId(order, orderStatusId) && canUpdateTransactionByOrderId(order, transactionStatusId)) {
            orderDAO.updateStatusByOrderId(order.getId(), orderStatusId, transactionStatusId);
            return true;
        }
        return false;
    }

    private boolean canUpdateStatusByOrderId(Order order, int orderStatusId) {
        OrderStatus orderStatus = OrderStatus.getByValue(orderStatusId);
        if (orderStatus == null) return false;
        List<OrderStatus> orderStatusCanChange = new ArrayList<>();
        switch (OrderStatus.getByValue(order.getOrderStatusId())) {
            case PENDING:
                orderStatusCanChange.add(OrderStatus.PENDING);
                orderStatusCanChange.add(OrderStatus.CONFIRMED);
                orderStatusCanChange.add(OrderStatus.CANCELLED);
                break;
            case CONFIRMED:
                orderStatusCanChange.add(OrderStatus.DELIVERY);
                orderStatusCanChange.add(OrderStatus.CONFIRMED);
                break;
            case DELIVERY:
                orderStatusCanChange.add(OrderStatus.DELIVERY);
                orderStatusCanChange.add(OrderStatus.CANCELLED);
                orderStatusCanChange.add(OrderStatus.COMPLETED);
                break;
        }
        return orderStatusCanChange.contains(orderStatus);
    }

    private boolean canUpdateTransactionByOrderId(Order order, int transactionStatusId) {
        TransactionStatus transactionStatus = TransactionStatus.getByValue(transactionStatusId);
        if (transactionStatus == null) return false;
        List<TransactionStatus> transactionStatusCanChange = new ArrayList<>();
        switch (TransactionStatus.getByValue(order.getTransactionStatusId())) {
            case UN_PAID, PROCESSING:
                transactionStatusCanChange.add(TransactionStatus.UN_PAID);
                transactionStatusCanChange.add(TransactionStatus.PROCESSING);
                transactionStatusCanChange.add(TransactionStatus.PAID);
                break;
        }
        return transactionStatusCanChange.contains(transactionStatus);
    }

    public boolean updateOrderTransaction(Order order, int transactionStatusId) {
        if (canUpdateTransactionByOrderId(order, transactionStatusId)) {
            orderDAO.updateOrderTransaction(order.getId(), transactionStatusId);
            return true;
        }
        return false;
    }

    public boolean updateOrderStatus(Order order, int statusId) {
        if (canUpdateStatusByOrderId(order, statusId)) {
            orderDAO.updateOrderStatus(order.getId(), statusId);
            return true;
        }
        return false;
    }

    public Voucher getVoucherById(int id) {
        return orderDAO.getVoucherById(id);
    }

    public String getTotalPriceFormatByOrderId(String orderId) {
        Order order = orderDAO.getOrderById(orderId);
        List<OrderDetail> listOrderDetail = orderDetailDAO.getListOrderDetailByOrderId(orderId);
        double totalPrice = 0;
        for (OrderDetail orderDetail : listOrderDetail) {
            totalPrice += orderDetail.getPrice();
        }

        if (order.getVoucherId() != 0) {
            Voucher voucher = getVoucherById(order.getVoucherId());
            totalPrice *= (1 - voucher.getDiscountPercent());
        }
        return FormatCurrency.vietNamCurrency(totalPrice);
    }

    public long getQuantity() {
        return orderDAO.getQuantity();
    }

    public List<Order> getLimit(int limit, int offset) {
        return orderDAO.getLimit(limit, offset);
    }
}
