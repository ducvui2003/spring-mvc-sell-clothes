package com.spring.websellspringmvc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewDetailResponse {
    ReviewResponse review;
    OrderDetailResponse orderDetail;
    OrderResponse order;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewResponse {
        int id;
        String feedback;
        Date date;
        int ratingStar;
        boolean visibility;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailResponse {
        String productName;
        String image;
        String size;
        String color;
        String quantity;
        Double price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderResponse {
        String fullName;
        String email;
        String phone;
        String province;
        String district;
        String ward;
        String detail;

    }
}
