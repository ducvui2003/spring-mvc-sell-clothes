package com.spring.websellspringmvc.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDetailItemResponse implements Serializable {
    int id;
    String name;
    String size;
    String color;
    int quantity;
    double price;
    String thumbnail;
}
