package com.spring.websellspringmvc.dto.mvc.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCardResponse {
    int id;
    String name;
    String description;
    double salePrice;
    double originalPrice;
    String thumbnail;
    int rating;
    int reviewCount;
}
