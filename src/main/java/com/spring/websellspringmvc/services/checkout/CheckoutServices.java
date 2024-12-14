package com.spring.websellspringmvc.services.checkout;

import com.spring.websellspringmvc.dto.mvc.request.CheckoutFormData;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.models.DeliveryMethod;
import com.spring.websellspringmvc.models.PaymentMethod;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface CheckoutServices {
    List<CartItemResponse> getCarts(List<Integer> listCartItemId, Integer userId);

    void createOrder(CheckoutFormData request, Integer userId);

    String createOrderByVnPay(CheckoutFormData request, Integer userId, String ip) throws UnsupportedEncodingException;

    void updateTransactionStatusVNPay(String paymentRef, TransactionStatus status);
}
