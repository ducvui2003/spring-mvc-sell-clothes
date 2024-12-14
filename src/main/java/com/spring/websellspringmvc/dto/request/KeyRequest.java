package com.spring.websellspringmvc.dto.request;

import jakarta.mail.Multipart;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class KeyRequest {
    @NotBlank(message = "File không được để trống")
    MultipartFile keyFile;
    @NotBlank(message = "Xem trước khóa mới không được để trống")
    String previewKey;

}
