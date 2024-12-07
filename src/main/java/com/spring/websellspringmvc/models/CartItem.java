package com.spring.websellspringmvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CartItem {
    Integer id;
    @ColumnName("cart_id")
    Integer cartId;
    @ColumnName("product_id")
    Integer productId;
    Integer sizeId;
    @ColumnName("color_id")
    Integer colorId;
    Integer quantity;

}
