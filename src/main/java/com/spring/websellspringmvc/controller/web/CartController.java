package com.spring.websellspringmvc.controller.web;


import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.dto.mvc.request.CheckoutRequest;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.services.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller("cartControllerWeb")
@RequiredArgsConstructor
@RequestMapping("/cart")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @GetMapping
    public ModelAndView getCartPage() {
        ModelAndView modelAndView = new ModelAndView(ConfigPage.USER_CART);
        List<CartItemResponse> carts = cartService.getCart();
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        modelAndView.addObject("carts", carts);
        modelAndView.addObject("checkout", checkoutRequest);
        return modelAndView;
    }
}
