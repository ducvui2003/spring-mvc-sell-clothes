package com.spring.websellspringmvc.dto.request;


import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateVoucherRequest {
    String code;
    Integer availableTurns;
    String description;
    Integer minimumPrice;
    Double discountPercent;
    Date expiryDate;
    String state;
    Integer[] productId;
}