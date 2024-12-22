package com.spring.websellspringmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.websellspringmvc.utils.constraint.Gender;
import com.spring.websellspringmvc.utils.constraint.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserDetailResponse {
    String id;
    String username;
    String email;
    String fullName;
    Gender gender;
    Date birthday;
    Role role;
    String phone;
    AdminUserKey key;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AdminUserKey {

    }
}
