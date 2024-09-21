package com.spring.websellspringmvc.controller.web.admin;

import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminUserControllerMVC")
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {

    @GetMapping
    public ModelAndView showUserPage() {
        ModelAndView mav = new ModelAndView(PageAddress.ADMIN_USER.getPage());
        return mav;
    }
}
