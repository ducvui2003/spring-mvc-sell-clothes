package com.spring.websellspringmvc.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    int id;
    String orderId;
    int productId;
    String productName;
    String sizeRequired;
    String colorRequired;
    int quantityRequired;
    double price;
}
