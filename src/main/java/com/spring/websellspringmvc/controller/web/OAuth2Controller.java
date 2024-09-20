package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.services.authentication.FacebookServices;
import com.spring.websellspringmvc.services.authentication.GoogleServices;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OAuth2Controller {
    @Qualifier("facebookService")
    FacebookServices facebookServices;
    @Qualifier("googleService")
    GoogleServices googleLoginServices;
    @Value("${app.service.google.login-uri}")
    @NonFinal
    String loginUriGoogle;
    @Value("${app.service.facebook.login-uri}")
    @NonFinal
    String loginUriFacebook;

    @GetMapping("/oauth2/google/login")
    public RedirectView loginGoogle() {
        return new RedirectView(loginUriGoogle);
    }

    @GetMapping("/oauth2/google/login/callback")
    public RedirectView loginGoogleCallback(@RequestParam("code") String code) throws IOException {
        googleLoginServices.signIn(code);
        return new RedirectView(ConfigPage.HOME);
    }

    @GetMapping("/oauth2/facebook/login")
    public RedirectView loginFacebook() {
        return new RedirectView(loginUriFacebook);
    }

    @GetMapping("/oauth2/facebook/login/callback")
    public RedirectView loginFacebookCallback(@RequestParam("code") String code) throws IOException {
        facebookServices.signIn(code);
        return new RedirectView(ConfigPage.HOME);
    }
}
