package com.spring.websellspringmvc.utils;

import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.UserServices;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    static UserServices userServices;

    public static User getUserById(int userId) {
        return userServices.getUser(userId);
    }

    public static User getUserByIdProductDetail(int orderDetailId) {
        return userServices.getUserByIdProductDetail(orderDetailId);
    }
}
