package com.spring.websellspringmvc.services.order;

import com.spring.websellspringmvc.dto.request.ChangeOrderRequest;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderResponse;
import com.spring.websellspringmvc.models.Size;

import java.util.List;

public interface OrderServices {
    List<OrderResponse> getOrder(int userId, int statusOrder);

    List<OrderDetailItemResponse> getOrderDetailByOrderId(String orderId);

    OrderDetailResponse getOrderByOrderId(String orderId, int userId);

    void changeOrder(String orderId, Integer userId, ChangeOrderRequest request);

    void updateOrderStatusVerify(String orderId, int userId);

}
