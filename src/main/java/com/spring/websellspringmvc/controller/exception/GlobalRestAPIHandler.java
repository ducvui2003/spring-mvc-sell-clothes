package com.spring.websellspringmvc.controller.exception;

import com.spring.websellspringmvc.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.spring.websellspringmvc.controller.api","com.spring.websellspringmvc.passkey.controller" })
public class GlobalRestAPIHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getErrorCode().getCode())
                .body(new ApiResponse<>(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage(), null));
    }
}
