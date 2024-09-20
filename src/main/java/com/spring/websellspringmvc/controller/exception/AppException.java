package com.spring.websellspringmvc.controller.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    private ErrorView errorView;


    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorView errorView) {
        super();
        this.errorView = errorView;
    }
}
