package com.spring.websellspringmvc.services.user;

import com.spring.websellspringmvc.dto.response.UserInfoResponse;

public interface UserServices {
    UserInfoResponse getUserInfo(int id);
}
