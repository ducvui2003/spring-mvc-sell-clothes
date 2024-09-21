package com.spring.websellspringmvc.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

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
    LocalDate birthDay;
    boolean verify;
    String role;
    String avatar;
    String tokenVerify;
    Timestamp tokenVerifyTime;
    String tokenResetPassword;
    Timestamp tokenResetPasswordTime;
}
