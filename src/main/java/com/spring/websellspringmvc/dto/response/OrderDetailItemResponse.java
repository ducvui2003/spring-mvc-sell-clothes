package com.spring.websellspringmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailItemResponse implements Serializable {
    Integer id;
    Integer productId;
    String name;
    String size;
    String color;
    int quantity;
    double price;
    String thumbnail;
}
