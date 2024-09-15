package com.spring.websellspringmvc.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable {
    int id;
    String username;
    String passwordEncoding;
    String fullName;
    String gender;
    String email;
    String phone;
    String address;
    Date birthDay;
    boolean isVerify;
    String role;
    String avatar;
    String tokenVerify;
    Timestamp tokenVerifyTime;
    String tokenResetPassword;
    Timestamp tokenResetPasswordTime;

}
