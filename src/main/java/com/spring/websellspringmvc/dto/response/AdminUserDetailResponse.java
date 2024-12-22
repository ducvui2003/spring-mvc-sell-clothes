package com.spring.websellspringmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.websellspringmvc.utils.constraint.Gender;
import com.spring.websellspringmvc.utils.constraint.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

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
    @JsonFormat(pattern = "dd-MM-yyyy")
    Date birthDay;
    Role role;
    String phone;
    List<AdminUserKey> keys;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AdminUserKey {
        String id;
        String publicKey;
        @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
        LocalDateTime createdAt;
        @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
        LocalDateTime updatedAt;
        boolean inUse;
    }
}
