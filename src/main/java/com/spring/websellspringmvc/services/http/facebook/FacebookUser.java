package com.spring.websellspringmvc.services.http.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class FacebookUser {
    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;

    @JsonProperty("email")
    String email;
}
