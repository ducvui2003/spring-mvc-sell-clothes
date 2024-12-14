package com.spring.websellspringmvc.services.user;

import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.dto.response.UserInfoResponse;
import com.spring.websellspringmvc.mapper.UserMapper;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserServicesImpl implements UserServices {
    UserDAO userDAO;
    UserMapper userMapper = UserMapper.INSTANCE;
    CloudinaryUploadServices cloudinaryUploadServices;

    @Override
    public UserInfoResponse getUserInfo(int id) {
        User user = userDAO.findById(id);
        if (user.getAvatar() != null)
            user.setAvatar(cloudinaryUploadServices.getImage(ImagePath.USER.getPath(), user.getAvatar()));
        return userMapper.toUserInfoResponse(user);
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
