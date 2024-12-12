package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.dto.mvc.response.ProductDetailResponse;
import com.spring.websellspringmvc.dto.request.datatable.DatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.ProductDetailAdminResponse;
import com.spring.websellspringmvc.dto.response.datatable.ProductDatatable;
import com.spring.websellspringmvc.models.ProductFilter;
import com.spring.websellspringmvc.models.Slider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<ProductCardResponse> getListNewProducts(Pageable pageable);

    List<Slider> getListSlideShow();

    Page<ProductCardResponse> getListTrendProducts(Pageable pageable);

    ProductDetailResponse getProductDetail(int productId);

    Page<ProductCardResponse> filter(ProductFilter productFilter);

    DatatableResponse<ProductDatatable> datatable(DatatableRequest datatableRequest);

    void changeVisibility(int productId, boolean visibility);

    ProductDetailAdminResponse getProductDetailAdmin(int productId);
}
