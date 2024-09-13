package com.spring.websellspringmvc.utils;

import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.UserServices;

public class UserFactory {
    public static User getUserById(int userId) {
        return UserServices.getINSTANCE().getUser(userId);
    }

    public static User getUserByIdProductDetail(int orderDetailId) {
        return UserServices.getINSTANCE().getUserByIdProductDetail(orderDetailId);
    }
}
