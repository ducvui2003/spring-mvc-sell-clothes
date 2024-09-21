package com.spring.websellspringmvc.dto.mvc.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    int id;
    String name;
    String description;
    double salePrice;
    double originalPrice;
    List<String> images;
    int rating;
    int reviewCount;
    List<Size> sizes;
    List<Color> colors;

    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    public static class Size {
        int id;
        String name;
        double price;
    }

    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    public static class Color {
        int id;
        String code;
    }
}
