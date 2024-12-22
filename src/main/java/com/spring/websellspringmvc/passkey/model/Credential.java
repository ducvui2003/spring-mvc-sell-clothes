package com.spring.websellspringmvc.passkey.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Credential {
    String id;
    String name;
    String userId;
    String publicKey;
    long signCount;
    String type;
    @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy ")
    LocalDateTime createdAt;
}
