package com.spring.websellspringmvc.dto.response.datatable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductDataTable {
    int id;
    String name;
    String category;
    Double originalPrice;
    Double salePrice;
    String quantity;
    String visibility;
}
