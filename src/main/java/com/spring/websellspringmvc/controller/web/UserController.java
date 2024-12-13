package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.utils.constraint.PageAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("userControllerMvc")
@RequestMapping("/user")
public class UserController {

    @GetMapping(value = {
            "/info",
            "/"
    })
    public ModelAndView getInfoPage() {
        ModelAndView mov = new ModelAndView();
        mov.setViewName(PageAddress.USER_INFO.getPage());
        return mov;
    }

    @GetMapping("/security")
    public ModelAndView getSecurityPage() {
        return new ModelAndView(PageAddress.USER_SECURITY.getPage());
    }

    @GetMapping("/order")
    public ModelAndView getOrderPage() {
        return new ModelAndView(PageAddress.USER_ORDER.getPage());
    }

    @GetMapping("/key")
    public ModelAndView getKeyPage() {
        return new ModelAndView(PageAddress.USER_KEY.getPage());
    }
}
