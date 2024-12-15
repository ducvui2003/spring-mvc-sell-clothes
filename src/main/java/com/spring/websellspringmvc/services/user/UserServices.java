package com.spring.websellspringmvc.services.user;

import com.spring.websellspringmvc.dto.response.UserInfoResponse;
import com.spring.websellspringmvc.models.Key;

public interface UserServices {
    UserInfoResponse getUserInfo(int id);
    void changePassword(int userId, String password);
}
