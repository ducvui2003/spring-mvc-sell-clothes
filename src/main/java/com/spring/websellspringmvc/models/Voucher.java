package com.spring.websellspringmvc.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher {
    Integer id;
    String code;
    String description;
    double minimumPrice;
    double discountPercent;
    Date expiryDate;
    int availableTurns;
    String state;
    Date createAt;
}
