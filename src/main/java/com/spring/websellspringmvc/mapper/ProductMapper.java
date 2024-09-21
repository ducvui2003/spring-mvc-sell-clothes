package com.spring.websellspringmvc.mapper;

import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.dto.mvc.response.ProductDetailResponse;
import com.spring.websellspringmvc.dto.request.CreateProductRequest;
import com.spring.websellspringmvc.dto.request.UpdateProductRequest;
import com.spring.websellspringmvc.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toProduct(CreateProductRequest request);

    Product toProduct(UpdateProductRequest request);

    ProductCardResponse toProductCardResponse(Product product);

    ProductDetailResponse toProductDetailResponse(Product product);
}
