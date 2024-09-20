package com.spring.websellspringmvc.services.authentication;

import com.spring.websellspringmvc.dto.mvc.request.ForgetPasswordRequest;
import com.spring.websellspringmvc.dto.mvc.request.ResetPasswordRequest;

public interface PasswordService {
    void forgetPassword(ForgetPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
