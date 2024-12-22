package com.spring.websellspringmvc.dto.response.datatable;

import com.spring.websellspringmvc.utils.constraint.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserDatatable {
    int id;
    String username;
    String email;
    String fullName;
    Gender gender;
    String phone;
    String role;
}
