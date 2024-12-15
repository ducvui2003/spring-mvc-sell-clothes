package com.spring.websellspringmvc.passkey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Credential {
    int id;
    int userId;
    byte[] credentialId;
    LocalDate crateAt;
    String publicKey;
    int signCount;
}
