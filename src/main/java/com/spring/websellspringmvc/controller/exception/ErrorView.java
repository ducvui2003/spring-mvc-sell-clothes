package com.spring.websellspringmvc.controller.exception;

import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorView {
    Map<String, String> errors;
    String viewName;
    Map<String, Object> attributes;

    public static final ErrorView SIGN_IN_FAILED = new ErrorView(
            Map.of("signInFailed", "Tài khoản hoặc mật khẩu sai"),
            PageAddress.SIGN_IN.getPage()
    );

    public static final ErrorView SIGN_UP_FAILED = new ErrorView(
            Map.of("user exist ", "username hoặc email đã tồn tại"),
            PageAddress.SIGN_UP.getPage()
    );

    public static final ErrorView ERROR_404 = new ErrorView(
            PageAddress.ERROR_404.getPage()
    );

    public static final ErrorView FORGET_PASSWORD_FAILED = new ErrorView(
            Map.of("email", "Email không tồn tại"),
            PageAddress.FORGET_PASSWORD.getPage()
    );


    public ErrorView(String viewName) {
        this.viewName = viewName;
    }

    public ErrorView(ErrorView errorView, String key, Object dto) {
        this.errors = errorView.getErrors();
        this.viewName = errorView.getViewName();
        this.attributes = Map.of(key, dto);
    }

    ErrorView(Map<String, String> errors, String viewName) {
        this.errors = errors;
        this.viewName = viewName;
    }

    ErrorView(ErrorView errorView, Map<String, String> errors) {
        this.viewName = errorView.getViewName();
        this.errors = errors;
    }

}
