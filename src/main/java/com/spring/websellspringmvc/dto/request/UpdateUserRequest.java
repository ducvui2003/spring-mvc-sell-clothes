package com.spring.websellspringmvc.dto.request;

import java.sql.Date;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateUserRequest {
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
