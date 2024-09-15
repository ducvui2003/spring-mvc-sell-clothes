package com.spring.websellspringmvc.controller.exception;

import com.spring.websellspringmvc.config.ConfigPage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "com.spring.websellspringmvc.controller.web")
public class GlobalMvcHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException() {
        return new ModelAndView(ConfigPage.ERROR_404);
    }
}
