package com.spring.websellspringmvc.dto.response;

import com.spring.websellspringmvc.models.Color;
import com.spring.websellspringmvc.models.Image;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductDetailAdminResponse {
    int id;
    String name;
    int categoryId;
    String description;
    double originalPrice;
    double salePrice;
    boolean visibility;
    List<Size> sizes;
    List<Color> colors;
    List<Image> images;
}
