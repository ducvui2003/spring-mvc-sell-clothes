package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dto.request.datatable.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.OrderStatus;
import com.spring.websellspringmvc.models.PaymentMethod;
import com.spring.websellspringmvc.models.TransactionStatus;

import java.util.List;

public interface AdminOrderServices {
    DatatableResponse<OrderDatatable> datatable(OrderDatatableRequest request);

    AdminOrderDetailResponse getOrderDetail(String orderId);

    List<AdminOrderDetailResponse> getOrderPrevious(String orderId);

    List<OrderStatus> getListAllOrderStatus();

    List<TransactionStatus> getListAllTransactionStatus();

    List<com.spring.websellspringmvc.utils.constraint.OrderStatus> getOrderStatusCanChangeByOrderId(String orderId);

    List<com.spring.websellspringmvc.utils.constraint.TransactionStatus> getTransactionStatusCanChangeByOrderId(String orderId);

}
