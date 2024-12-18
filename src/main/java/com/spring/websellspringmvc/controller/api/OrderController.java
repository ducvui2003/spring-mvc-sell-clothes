package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.request.ChangeOrderRequest;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderResponse;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.order.OrderServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderServices orderServices;
    SessionManager sessionManager;

    @GetMapping("/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrder(@PathVariable("status") OrderStatus status) throws ServletException, IOException {
        User user = sessionManager.getUser();
        List<OrderResponse> orders = orderServices.getOrder(user.getId(), status.getValue());
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK.value(), "success", orders));
    }

    @GetMapping("/detail/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(@PathVariable("orderId") String orderId) throws ServletException, IOException {
        User user = sessionManager.getUser();
        int userId = user.getId();
        OrderDetailResponse orderDetail = orderServices.getOrderByOrderId(orderId, userId);
        if (orderDetail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<OrderDetailResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Order not found")
                    .build());
        } else {
            return ResponseEntity.ok(ApiResponse.<OrderDetailResponse>builder()
                    .code(HttpServletResponse.SC_OK)
                    .message("Order found")
                    .data(orderDetail)
                    .build());
        }
    }

    @PutMapping("/address/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> changeAddressOrder(@PathVariable("orderId") String orderId, @RequestBody ChangeOrderRequest request
    ) {
        User user = sessionManager.getUser();
        int userId = user.getId();
        orderServices.changeOrder(orderId, userId, request);
        return ResponseEntity.ok(ApiResponse.<OrderDetailResponse>builder()
                .code(HttpServletResponse.SC_OK)
                .message("Order found")
                .build());
    }
}
