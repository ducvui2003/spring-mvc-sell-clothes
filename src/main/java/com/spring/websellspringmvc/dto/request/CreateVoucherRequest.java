package com.spring.websellspringmvc.dto.request;

import java.sql.Date;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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
