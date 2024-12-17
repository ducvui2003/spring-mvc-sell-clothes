package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.dto.mvc.request.ForgetPasswordRequest;
import com.spring.websellspringmvc.dto.mvc.request.ResetPasswordRequest;
import com.spring.websellspringmvc.services.authentication.PasswordService;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordController {
    PasswordService passwordService;

    @GetMapping("/forgetPassword")
    public ModelAndView forgetPassword() {
        return new ModelAndView(PageAddress.FORGET_PASSWORD.getPage());
    }

    @PostMapping("/forgetPassword")
    public ModelAndView forgetPassword(@ModelAttribute("form") ForgetPasswordRequest request) {
        passwordService.forgetPassword(request);
        return new ModelAndView(PageAddress.FORGET_PASSWORD.getPage())
                .addObject("sendMail", "Email đã được gửi đến hộp thư của bạn")
                .addObject("form", new ForgetPasswordRequest());
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String tokenResetPassword = request.getParameter("token-reset-password");
//        boolean status = passwordService.resetPassword(email, tokenResetPassword);
//        if (status) {
//            request.setAttribute("email", email);
//            request.setAttribute("token", tokenResetPassword);
//            return new ModelAndView(ConfigPage.RESET_PASSWORD)
//                    .addObject("email", email)
//                    .addObject("token", tokenResetPassword)
//                    .addObject("form", ResetPasswordRequest.builder().email(email).build());
//        } else
        return new ModelAndView("redirect:/error-404");
    }

    @PostMapping("/updatePassword")
    public ModelAndView updatePassword(@ModelAttribute("form") ResetPasswordRequest request) {
        passwordService.resetPassword(request);
        return new ModelAndView(PageAddress.RESET_PASSWORD.getPage())
                .addObject("form", new ResetPasswordRequest())
                .addObject("updateSuccess", "Cập nhập mật khẩu thành công.");
    }
}
