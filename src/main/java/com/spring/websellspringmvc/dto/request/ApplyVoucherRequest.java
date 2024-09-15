package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyVoucherRequest {
    @NotNull
    String code;
    @JsonProperty("id[]")
    int[] id;
}
