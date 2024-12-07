package com.spring.websellspringmvc.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AddCartRequest {
    Integer productId;
    Integer colorId;
    Integer quantity;
    Integer sizeId;
}

