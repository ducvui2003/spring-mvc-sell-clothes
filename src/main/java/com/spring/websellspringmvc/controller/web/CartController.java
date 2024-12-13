package com.spring.websellspringmvc.controller.web;


import com.spring.websellspringmvc.dto.mvc.request.CheckoutFormData;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.services.cart.CartService;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller("cartControllerWeb")
@RequiredArgsConstructor
@RequestMapping("/cart")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @GetMapping
    public ModelAndView getCartPage() {
        ModelAndView modelAndView = new ModelAndView(PageAddress.USER_CART.getPage());
        List<CartItemResponse> carts = cartService.getCart();
        CheckoutFormData checkoutFormData = new CheckoutFormData();
        modelAndView.addObject("carts", carts);
        modelAndView.addObject("checkout", checkoutFormData);
        return modelAndView;
    }
}
