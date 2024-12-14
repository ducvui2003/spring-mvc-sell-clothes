package com.spring.websellspringmvc.mapper;

import com.spring.websellspringmvc.dto.mvc.request.SignUpRequest;
import com.spring.websellspringmvc.dto.request.CreateUserRequest;
import com.spring.websellspringmvc.dto.request.UpdateUserRequest;
import com.spring.websellspringmvc.dto.response.UserInfoResponse;
import com.spring.websellspringmvc.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(CreateUserRequest createUserRequest);

    User toUser(UpdateUserRequest updateUserRequest);

    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "birthDay", source = "dob")
    User toUser(SignUpRequest signUpRequest);

    UserInfoResponse toUserInfoResponse(User user);
}
