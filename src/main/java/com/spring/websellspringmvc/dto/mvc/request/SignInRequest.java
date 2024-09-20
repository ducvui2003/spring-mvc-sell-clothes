package com.spring.websellspringmvc.dto.mvc.request;

import com.spring.websellspringmvc.utils.anotation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SignInRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    @PasswordValid(message = "Mật khẩu cần chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt")
    String password;
}
