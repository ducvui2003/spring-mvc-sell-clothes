package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.mvc.request.CheckoutRequest;
import com.spring.websellspringmvc.services.checkout.CheckoutServices;
import com.spring.websellspringmvc.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController("checkoutControllerApi")
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckoutController {
    CheckoutServices checkoutServices;
    SessionManager sessionManager;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCheckout(@RequestBody CheckoutRequest request) {
        int userId = sessionManager.getUser().getId();
        checkoutServices.createOrder(request, userId);
        return ResponseEntity.ok(ApiResponse.<String>builder().build());
    }

    @PostMapping("/vn-pay")
    public ResponseEntity<ApiResponse<String>> createCheckout(@RequestBody CheckoutRequest request, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        int userId = sessionManager.getUser().getId();
        String ip = httpServletRequest.getRemoteAddr();
        String url = checkoutServices.createOrderByVnPay(request, userId, ip);
        return ResponseEntity.ok(ApiResponse.<String>builder().code(200).message("Vn pay url g").data(url).build());
    }
}
