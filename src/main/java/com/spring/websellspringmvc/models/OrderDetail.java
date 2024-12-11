package com.spring.websellspringmvc.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
