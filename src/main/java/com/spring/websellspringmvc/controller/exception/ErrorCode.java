package com.spring.websellspringmvc.controller.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    PRODUCT_NOT_FOUND(404, "Product not found"),
    UPDATE_SUCCESS(200, "Update success"),
    CREATE_SUCCESS(200, "Create success"),
    SIZE_ERROR(1001, "Size error"),
    COLOR_ERROR(1002, "Color error"),
    CATEGORY_ERROR(1003, "Category error"),
    MISSING_REQUEST(1004, "Empty request"),
    ERROR_PARAM_REQUEST(1004, "Param not valid"),
    PARAMETER_ERROR(1005, "Parameter error"),
    IMAGE_ERROR(1005, "Image error"),
    PRICE_ERROR(1005, "Price error"),
    QUANTITY_ERROR(1005, "Quantity error"),
    NOT_VALID(HttpStatus.BAD_REQUEST.value(), "Not valid"),
    CREATE_FAILED(HttpStatus.CONFLICT.value(), "Create Failed"),
    UPDATE_FAILED(HttpStatus.CONFLICT.value(), "Update Failed"),
    DELETE_FAILED(HttpStatus.CONFLICT.value(), "Delete Failed");

    private Integer code;
    private String message;


    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
