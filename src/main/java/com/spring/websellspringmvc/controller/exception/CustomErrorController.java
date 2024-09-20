package com.spring.websellspringmvc.controller.exception;

import com.spring.websellspringmvc.config.ConfigPage;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView(ConfigPage.ERROR_404);
    }

    @GetMapping("/error-404")
    public ModelAndView notFoundPage() {
        return new ModelAndView(ConfigPage.ERROR_404);
    }
}
