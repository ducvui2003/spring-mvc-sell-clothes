package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.dto.mvc.response.ProductDetailResponse;
import com.spring.websellspringmvc.dto.request.DatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.ProductDataTable;
import com.spring.websellspringmvc.models.ProductFilter;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductDetailResponse getProductDetail(int productId);

    Page<ProductCardResponse> filter(ProductFilter productFilter);

    DatatableResponse<ProductDataTable> datatable(DatatableRequest datatableRequest);
}
