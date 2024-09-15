package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Category is required")
    Integer idCategory;
    @Min(value = 1, message = "The number must be greater than 0")
    Double originalPrice;
    @Min(value = 1, message = "The number must be greater than 0")
    Double salePrice;
    String description;
    @JsonProperty("nameSize[]")
    String[] nameSize;
    @JsonProperty("sizePrice[]")
    Double[] sizePrice;
    String[] color;
    @JsonProperty("nameImageAdded[]")
    String[] nameImageAdded;
}
