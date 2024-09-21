package com.spring.websellspringmvc.services.http.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUser {
    String id;
    String email;
    @JsonProperty("verified_email")
    boolean verifyEmail;
    @JsonProperty("name")
    String fullName;
    @JsonProperty("given_name")
    String firstName;
    @JsonProperty("family_name")
    String lastName;
    String picture;
    String locale;
    String hd;

}

