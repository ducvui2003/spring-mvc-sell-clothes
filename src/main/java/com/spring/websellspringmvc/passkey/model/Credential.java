package com.spring.websellspringmvc.passkey.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Credential {
    String id;
    String userId;
    String publicKey;
    long signCount;
    String type;
    LocalDate createAt;
}
