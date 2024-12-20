package com.spring.websellspringmvc.passkey.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RegistrationStartRequest {
    String email;
    @JsonProperty("fullName")
    String username;
}
