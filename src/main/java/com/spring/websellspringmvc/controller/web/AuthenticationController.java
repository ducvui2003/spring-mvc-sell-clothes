package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.controller.exception.ErrorView;
import com.spring.websellspringmvc.controller.exception.ErrorForm;
import com.spring.websellspringmvc.dto.mvc.request.SignInRequest;
import com.spring.websellspringmvc.dto.mvc.request.SignUpRequest;
import com.spring.websellspringmvc.services.authentication.AuthenticationService;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    SessionManager sessionManager;


    @GetMapping("/signIn")
    public ModelAndView signInPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(PageAddress.SIGN_IN.getPage());
        mav.addObject("user", new SignInRequest());
        return mav;
    }

    @PostMapping("/signIn")
    public ModelAndView signIn(@Valid @ModelAttribute("user") SignInRequest request, BindingResult bindingResult) throws ErrorForm {
        if (bindingResult.hasErrors()) {
            throw new ErrorForm(bindingResult, new ErrorView(ErrorView.SIGN_IN_FAILED,
                    "user", request));
        }
        authenticationService.signIn(request);

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/signUp")
    public ModelAndView signUpPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(PageAddress.SIGN_UP.getPage());
        mav.addObject("user", new SignUpRequest());
        return mav;
    }

    @PostMapping("/signUp")
    public ModelAndView signUp(@Valid @ModelAttribute("user") SignUpRequest request, BindingResult bindingResult) throws ErrorForm {
        if (bindingResult.hasErrors()) {
            throw new ErrorForm(bindingResult, new ErrorView(ErrorView.SIGN_UP_FAILED,
                    "user", request));
        }
        authenticationService.signUp(request);
        return new ModelAndView("redirect:/");
    }


    @PostMapping("/verify")
    public ModelAndView verify(HttpServletRequest request) {
        String username = request.getParameter("username");
        String token = request.getParameter("token-verify");
        log.info("username {} tokenVerify {}", username, token);
        request.setAttribute("username", username);
        authenticationService.verify(username, token);
        return new ModelAndView("redirect:" + PageAddress.VERIFY_SUCCESS).addObject("username", username);
    }

    @GetMapping("/signOut")
    public ModelAndView signOut() {
        sessionManager.clearSession();
        return new ModelAndView("redirect:/");
    }
}
