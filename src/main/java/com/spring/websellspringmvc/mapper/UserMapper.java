package com.spring.websellspringmvc.mapper;

import com.spring.websellspringmvc.dto.request.CreateUserRequest;
import com.spring.websellspringmvc.dto.request.UpdateUserRequest;
import com.spring.websellspringmvc.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(CreateUserRequest createUserRequest);

    User toUser(UpdateUserRequest updateUserRequest);
}
