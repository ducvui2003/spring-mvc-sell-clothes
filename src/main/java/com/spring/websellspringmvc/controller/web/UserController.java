package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.dto.response.UserInfoResponse;
import com.spring.websellspringmvc.services.user.UserServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("userControllerMvc")
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {
    UserServices userServices;
    SessionManager sessionManager;

    @GetMapping(value = {
            "/info",
            "/"
    })
    public ModelAndView getInfoPage() {
        ModelAndView mov = new ModelAndView();
        int userId = sessionManager.getUser().getId();
        UserInfoResponse userInfoResponse = userServices.getUserInfo(userId);
        mov.setViewName(PageAddress.USER_INFO.getPage());
        mov.addObject("user", userInfoResponse);
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
