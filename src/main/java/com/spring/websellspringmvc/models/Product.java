package com.spring.websellspringmvc.models;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Product {
  int id;
  String name;
  int categoryId;
  String description;
  double originalPrice;
  double salePrice;
  boolean visibility;
  Date createAt;
}
