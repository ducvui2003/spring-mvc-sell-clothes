package com.spring.websellspringmvc.controller.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Getter
public class ErrorForm extends Throwable {
    String viewName;
    BindingResult bindingResult;
    Map<String, String> messages;
    ErrorView errorView;

    public ErrorForm(BindingResult bindingResult, ErrorView viewName) {
        this.bindingResult = bindingResult;
        this.viewName = viewName.getViewName();
        this.messages = viewName.getErrors();
        this.errorView = viewName;
    }
}
