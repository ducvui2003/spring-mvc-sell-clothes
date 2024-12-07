package com.spring.websellspringmvc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    int id;
    int productId;
    String name;
    String color;
    String size;
    int quantity;
    double price;
    double salePrice;
    String thumbnail;
}
