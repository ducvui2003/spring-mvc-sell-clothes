package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.request.AddCartRequest;
import com.spring.websellspringmvc.services.cart.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("cartControllerApi")
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;
    int DEFAULT_QUANTITY_CHANGE = 1;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addCart(@RequestBody AddCartRequest request) {
        cartService.create(request);
        return ResponseEntity.ok().body(ApiResponse.<Void>builder().code(HttpStatus.OK.value()).build());
    }

    @PostMapping("/increase/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> increase(@PathVariable("cartItemId") Integer cartItemId) {
        cartService.increase(cartItemId, DEFAULT_QUANTITY_CHANGE);
        return ResponseEntity.ok().body(ApiResponse.<Void>builder().code(HttpStatus.OK.value()).build());
    }

    @PostMapping("/decrease/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> decrease(@PathVariable("cartItemId") Integer productId) {
        cartService.decrease(productId, DEFAULT_QUANTITY_CHANGE);
        return ResponseEntity.ok().body(ApiResponse.<Void>builder().code(HttpStatus.OK.value()).build());
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("cartItemId") Integer productId) {
        cartService.delete(productId);
        return ResponseEntity.ok().body(ApiResponse.<Void>builder().code(HttpStatus.OK.value()).build());
    }
}
