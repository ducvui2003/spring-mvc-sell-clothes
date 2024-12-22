package com.spring.websellspringmvc.models;

import com.spring.websellspringmvc.utils.constraint.Gender;
import com.spring.websellspringmvc.utils.constraint.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jdbi.v3.core.enums.EnumByName;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 8074934311683817530L;
    int id;
    String username;
    String passwordEncoding;
    String fullName;
    Gender gender;
    String email;
    String phone;
    LocalDate birthDay;
    boolean verify;
    @EnumByName
    Role role;
    String avatar;
    String tokenVerify;
    Timestamp tokenVerifyTime;
    String tokenResetPassword;
    Timestamp tokenResetPasswordTime;
    String userHandle;
}
