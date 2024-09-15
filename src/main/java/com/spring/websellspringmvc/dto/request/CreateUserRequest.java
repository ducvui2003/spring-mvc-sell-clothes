package com.spring.websellspringmvc.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    String username;
    String password;
    String fullName;
    String gender;
    String email;
    String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date birthDay;
    String role;
}
