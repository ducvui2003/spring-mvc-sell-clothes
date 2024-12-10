package com.spring.websellspringmvc.services.checkout;

import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.models.DeliveryMethod;
import com.spring.websellspringmvc.models.PaymentMethod;

import java.util.List;

public interface CheckoutServices {
    List<DeliveryMethod> getAllInformationDeliveryMethod();

    List<PaymentMethod> getAllPaymentMethod();

    List<CartItemResponse> getCarts(List<Integer> listCartItemId, Integer userId);
}
