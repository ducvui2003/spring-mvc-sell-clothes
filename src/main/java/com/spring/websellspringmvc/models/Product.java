package com.spring.websellspringmvc.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    List<Image> images = new ArrayList<>();
}
