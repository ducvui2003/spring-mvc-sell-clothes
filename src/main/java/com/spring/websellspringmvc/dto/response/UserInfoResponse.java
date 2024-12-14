package com.spring.websellspringmvc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserInfoResponse {
    String username;
    String fullName;
    String gender;
    String email;
    String phone;
    Date birthDay;
    String role;
    String avatar;
}
