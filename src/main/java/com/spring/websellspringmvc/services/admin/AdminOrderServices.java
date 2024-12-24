package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dto.request.OrderStatusChangeRequest;
import com.spring.websellspringmvc.dto.request.datatable.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.StatusChangedResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.models.*;

import java.util.List;

public interface AdminOrderServices {
    DatatableResponse<OrderDatatable> datatable(OrderDatatableRequest request) throws Exception;

    AdminOrderDetailResponse getOrderDetail(String orderId);

    List<AdminOrderDetailResponse> getOrderPrevious(String orderId);

    List<OrderStatus> getListAllOrderStatus();

    List<TransactionStatus> getListAllTransactionStatus();

    List<OrderDetailItemResponse> getOrderDetailItems(String orderId);

    List<com.spring.websellspringmvc.utils.constraint.OrderStatus> getOrderStatusCanChangeByOrderId(String orderId);

    StatusChangedResponse getStatusCanChanged(String orderId);

    List<com.spring.websellspringmvc.utils.constraint.TransactionStatus> getTransactionStatusCanChangeByOrderId(String orderId);

    boolean changeStatus(String orderId, OrderStatusChangeRequest request);

}
