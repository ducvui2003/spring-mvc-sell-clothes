package com.spring.websellspringmvc.controller.exception;

import com.spring.websellspringmvc.utils.constraint.PageAddress;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Primary
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView(PageAddress.ERROR_404.getPage());
    }

    @GetMapping("/error-404")
    public ModelAndView notFoundPage() {
        return new ModelAndView(PageAddress.ERROR_404.getPage());
    }
}
