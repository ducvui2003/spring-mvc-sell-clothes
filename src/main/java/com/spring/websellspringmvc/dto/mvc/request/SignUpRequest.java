package com.spring.websellspringmvc.dto.mvc.request;

import com.spring.websellspringmvc.utils.anotation.FieldMatch;
import com.spring.websellspringmvc.utils.anotation.PasswordValid;
import com.spring.websellspringmvc.utils.constraint.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    String username;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;
    @NotBlank(message = "Số điện thoại không được để trống")
    String phone;
    @NotBlank(message = "Họ và tên không được để trống")
    String fullName;
    @NotNull(message = "Giới tính không được để trống")
    Gender gender;
    @NotNull(message = "Ngày sinh không đúng định dạng")
    @Past(message = "Ngày sinh không hợp lệ")
    LocalDate dob;
    @NotBlank(message = "Mật khẩu không được để trống")
    @PasswordValid(message = "Mật khẩu cần chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt")
    String password;
}
