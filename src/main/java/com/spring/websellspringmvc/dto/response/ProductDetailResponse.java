package com.spring.websellspringmvc.dto.response;

import com.spring.websellspringmvc.models.Color;
import com.spring.websellspringmvc.models.Image;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    Product product;
    List<Size> sizes;
    List<Color> colors;
    List<Image> images;
}
