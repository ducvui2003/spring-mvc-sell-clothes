package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest extends CreateProductRequest {
    @NotNull
    Integer id;
    @NotNull
    @JsonProperty("sizeId[]")
    Integer[] sizeId;
    @NotNull
    @JsonProperty("colorId[]")
    Integer[] colorId;
    @NotNull
    @JsonProperty("idImageDeleted[]")
    Integer[] idImageDeleted;

}
