package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.models.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserServices {
    UserDAO userDAO;

    public User getUser(int userId) {
        return userDAO.selectById(userId);
    }

    public void updateUserPassword(int userId, String password) {
        userDAO.updateUserPassword(userId, password);
    }

    public void updateUserByID(int id, String fullname, String gender, String phone, Date birthDay) {
        userDAO.updateUserById(id, fullname, gender, phone, birthDay);
    }

    public void updateInfoUser(int id, String avatar) {
        userDAO.updateInfoUser(id, avatar);
    }

    public User getUserByIdProductDetail(int orderDetailId) {
        List<User> listUser = userDAO.getUserByIdProductDetail(orderDetailId);
        if (listUser.isEmpty())
            return null;
        return listUser.get(0);
    }

    public void insertUser(User user) {
        userDAO.insert(user);
    }

    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    public long getQuantity() {
        return userDAO.getQuantity();
    }


    public List<User> getUser(Integer start, Integer length, String searchValue, String orderBy, String orderDir) {
        return userDAO.selectWithCondition(start, length, searchValue, orderBy, orderDir);
    }

    public long getTotalWithCondition(String searchValue) {
        return userDAO.getSizeWithCondition(searchValue);
    }
}
