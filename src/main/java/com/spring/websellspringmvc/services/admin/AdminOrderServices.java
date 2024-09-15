package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dao.OrderDetailDAO;
import com.spring.websellspringmvc.dao.OrderStatusDAO;
import com.spring.websellspringmvc.dao.TransactionStatusDAO;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.state.OrderState;
import com.spring.websellspringmvc.services.state.TransactionState;
import com.spring.websellspringmvc.utils.FormatCurrency;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderServices {
    OrderDAO orderDAO;
    OrderStatusDAO orderStatusDao;
    TransactionStatusDAO transactionStatusDao;
    OrderDetailDAO orderDetailDAO;

    public List<OrderStatus> getListAllOrderStatus() {
        return orderStatusDao.getListAllOrderStatus();
    }

    public List<TransactionStatus> getListAllTransactionStatus() {
        return transactionStatusDao.getListAllTransactionStatus();
    }

    public OrderStatus getOrderStatusById(int orderStatusId) {
        return orderStatusDao.getOrderStatusById(orderStatusId);
    }

    public TransactionStatus getTransactionStatusById(int transactionStatusId) {
        return transactionStatusDao.getTransactionStatusById(transactionStatusId);
    }

    public List<Order> getListAllOrders() {
        return orderDAO.getListAllOrders();
    }

    public List<Order> getListOrdersBySearchFilter(String[] paymentMethod, String[] orderStatus, String[] transactionStatus, String contentSearch, String searchSelect, String startDate, String endDate) {
        return orderDAO.getListOrdersBySearchFilter(paymentMethod, orderStatus, transactionStatus, contentSearch, searchSelect, startDate, endDate);
    }

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
        OrderState orderState = OrderState.getByValue(orderStatusId);
        if (orderState == null) return false;
        List<OrderState> orderStateCanChange = new ArrayList<>();
        switch (OrderState.getByValue(order.getOrderStatusId())) {
            case PENDING:
                orderStateCanChange.add(OrderState.PENDING);
                orderStateCanChange.add(OrderState.CONFIRMED);
                orderStateCanChange.add(OrderState.CANCELLED);
                break;
            case CONFIRMED:
                orderStateCanChange.add(OrderState.DELIVERY);
                orderStateCanChange.add(OrderState.CONFIRMED);
                break;
            case DELIVERY:
                orderStateCanChange.add(OrderState.DELIVERY);
                orderStateCanChange.add(OrderState.CANCELLED);
                orderStateCanChange.add(OrderState.COMPLETED);
                break;
        }
        return orderStateCanChange.contains(orderState);
    }

    private boolean canUpdateTransactionByOrderId(Order order, int transactionStatusId) {
        TransactionState transactionState = TransactionState.getByValue(transactionStatusId);
        if (transactionState == null) return false;
        List<TransactionState> transactionStateCanChange = new ArrayList<>();
        switch (TransactionState.getByValue(order.getTransactionStatusId())) {
            case UN_PAID, PROCESSING:
                transactionStateCanChange.add(TransactionState.UN_PAID);
                transactionStateCanChange.add(TransactionState.PROCESSING);
                transactionStateCanChange.add(TransactionState.PAID);
                break;
        }
        return transactionStateCanChange.contains(transactionState);
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
