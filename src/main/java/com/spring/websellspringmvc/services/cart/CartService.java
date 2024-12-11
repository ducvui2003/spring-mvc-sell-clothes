package com.spring.websellspringmvc.services.cart;

import com.spring.websellspringmvc.dto.request.AddCartRequest;
import com.spring.websellspringmvc.dto.response.CartItemResponse;

import java.util.List;

public interface CartService {
    List<CartItemResponse> getCart();

    void create(AddCartRequest addCartRequest);

    void delete(Integer cartItemId);

    void increase(Integer cartItemId, int quantity);

    void decrease(Integer cartItemId, int quantity);

}
