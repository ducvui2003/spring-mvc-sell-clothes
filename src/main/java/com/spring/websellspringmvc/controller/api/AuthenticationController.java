package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.controller.exception.ErrorForm;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.mvc.request.ForgetPasswordRequest;
import com.spring.websellspringmvc.dto.mvc.request.SignUpRequest;
import com.spring.websellspringmvc.services.authentication.AuthenticationService;
import com.spring.websellspringmvc.services.authentication.PasswordService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("authenticationControllerApi")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {
    AuthenticationService authenticationService;
    PasswordService passwordService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<?>> signUp(@Valid @RequestBody SignUpRequest request) {
        authenticationService.signUp(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Sign up success", null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
        passwordService.forgetPassword(request);;
        return ResponseEntity.ok(new ApiResponse<>(200, "Sign up success", null));
    }
}
