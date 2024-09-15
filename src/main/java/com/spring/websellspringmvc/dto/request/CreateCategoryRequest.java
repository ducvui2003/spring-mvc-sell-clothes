package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryRequest {
    @NotNull
    String nameCategory;
    @NotNull
    String sizeTableImage;
    @NotNull
    @JsonProperty("nameCategory[]")
    String[] nameParameter;
    @NotNull
    @JsonProperty("unit[]")
    String[] unit;
    @NotNull
    @JsonProperty("minValue[]")
    String[] minValue;
    @NotNull
    @JsonProperty("maxValue[]")
    @NotNull
    String[] maxValue;
    @NotNull
    @JsonProperty("guideImg[]")
    String[] guideImg;
}
