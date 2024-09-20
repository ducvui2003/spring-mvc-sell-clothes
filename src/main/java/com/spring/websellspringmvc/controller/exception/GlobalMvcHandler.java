package com.spring.websellspringmvc.controller.exception;

import com.spring.websellspringmvc.config.ConfigPage;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(basePackages = "com.spring.websellspringmvc.controller.web")
public class GlobalMvcHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException() {
        return new ModelAndView(ConfigPage.ERROR_404);
    }

    @ExceptionHandler(ErrorForm.class)
    public ModelAndView handleMethodArgumentNotValidException(ErrorForm ex) {
        ModelAndView model = new ModelAndView(ex.getViewName());
        model.addAllObjects(ex.getErrorView().getAttributes());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors)
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        model.addObject("errors", errors);
        return model;
    }

    @ExceptionHandler(AppException.class)
    public ModelAndView handleAppException(AppException ex) {
        ModelAndView model = new ModelAndView(ex.getErrorView().getViewName());
        model.addObject("errors", ex.getErrorView().getErrors());
        model.addAllObjects(ex.getErrorView().getAttributes());
        return model;
    }
}
