package com.spring.websellspringmvc.services.authentication;

import com.spring.websellspringmvc.dto.mvc.request.SignInRequest;
import com.spring.websellspringmvc.dto.mvc.request.SignUpRequest;
import com.spring.websellspringmvc.models.User;

public interface AuthenticationService {
    User signIn(SignInRequest request);

    void signUp(SignUpRequest request);

    void verify(String username, String token);
}
