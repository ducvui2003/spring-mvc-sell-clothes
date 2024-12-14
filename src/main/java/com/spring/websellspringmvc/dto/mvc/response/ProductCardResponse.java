package com.spring.websellspringmvc.dto.mvc.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
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
