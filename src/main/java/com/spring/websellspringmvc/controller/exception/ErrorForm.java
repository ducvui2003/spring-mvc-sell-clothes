package com.spring.websellspringmvc.controller.exception;

import java.util.Map;
import lombok.Getter;
import org.springframework.validation.BindingResult;

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
