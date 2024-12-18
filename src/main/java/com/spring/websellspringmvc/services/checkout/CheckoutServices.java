package com.spring.websellspringmvc.services.checkout;

import com.spring.websellspringmvc.dto.mvc.request.CheckoutRequest;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

public interface CheckoutServices {
    List<CartItemResponse> getCarts(List<Integer> listCartItemId, Integer userId);

    void createOrder(CheckoutRequest request, Integer userId);

    String createOrderByVnPay(CheckoutRequest request, Integer userId, String ip) throws UnsupportedEncodingException;

    void updateTransactionStatusVNPay(String paymentRef, TransactionStatus status);

    boolean verifyOrder(OrderDetailResponse orderDetail, List<AdminOrderDetailResponse> orderPrevious);

    double getFeeShipping(String provinceId, String districtId, String wardCode);

    LocalDateTime getLeadTime(String provinceId, String districtId, String wardCode);
}
