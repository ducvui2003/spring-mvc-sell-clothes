package com.spring.websellspringmvc.passkey.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationStartRequest {
    String email;
    @JsonProperty("fullName")
    String username;
}
