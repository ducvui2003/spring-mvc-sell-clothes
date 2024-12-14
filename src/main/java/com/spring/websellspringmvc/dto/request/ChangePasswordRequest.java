package com.spring.websellspringmvc.dto.request;

import com.spring.websellspringmvc.utils.anotation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Mật khẩu cũ không được để trống")
    String currentPassword;
    @NotBlank(message = "Mật khẩu mới không được để trống")
    @PasswordValid(message = "Mật khẩu cần chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt")
    String newPassword;
}
