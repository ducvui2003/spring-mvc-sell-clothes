package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.dto.mvc.request.CheckoutRequest;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.models.Address;
import com.spring.websellspringmvc.services.AddressServices;
import com.spring.websellspringmvc.services.checkout.CheckoutServices;
import com.spring.websellspringmvc.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CheckoutController {
    AddressServices addressServices;
    CheckoutServices checkoutServices;
    SessionManager sessionManager;

    @PostMapping("/checkout")
    public ModelAndView checkout(@ModelAttribute("checkout") CheckoutRequest request) {
        int userId = sessionManager.getUser().getId();
        List<Address> addresses = addressServices.getAddress(userId);
        List<CartItemResponse> cartItems = checkoutServices.getCarts(request.getCartItemId(), userId);
        ModelAndView mov = new ModelAndView();
        mov.setViewName(ConfigPage.CHECKOUT);
        mov.addObject("cartItems", cartItems);
        mov.addObject("addresses", addresses);
        return mov;
    }

}
