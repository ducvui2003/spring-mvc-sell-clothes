package com.spring.websellspringmvc.dto.mvc.request;

import com.spring.websellspringmvc.utils.anotation.FieldMatch;
import com.spring.websellspringmvc.utils.anotation.PasswordValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "password", second = "confirmPassword", message = "Mật khẩu không khớp")
public class ResetPasswordRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;
    @NotBlank(message = "Mật khẩu không được để trống")
    @PasswordValid(message = "Mật khẩu cần chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt")
    String password;
    String confirmPassword;
}
