package com.spring.websellspringmvc.services.user;

import com.spring.websellspringmvc.dto.response.UserInfoResponse;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.passkey.model.Credential;

import java.util.Optional;

public interface UserServices {
    UserInfoResponse getUserInfo(int id);

    void changePassword(int userId, String password);

    Optional<User> findByEmail(String email, boolean verify);

    void updateUserHandle(Integer id, String userHandle);

}
