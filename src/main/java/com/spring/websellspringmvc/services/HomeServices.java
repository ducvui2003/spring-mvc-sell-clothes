package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.HomeDAO;
import com.spring.websellspringmvc.dao.ImageDAO;
import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.mapper.ProductMapper;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomeServices {
    HomeDAO homeDao;
    ProductCardServices productCardServices;
    CloudinaryUploadServices cloudinaryUploadServices;
    ImageDAO imageDAO;
    ProductMapper productMapper = ProductMapper.INSTANCE;


//    public List<Map<String, Object>> getListTrendingProducts(boolean isSeeMore){
//        return homeDao.getListTrendingProducts(isSeeMore);
//    }

//    public List<Map<String, Object>> getListNewProducts(boolean isSeeMore){
//        return homeDao.getListNewProducts(isSeeMore);
//    }

    public List<ProductCardResponse> getListNewProducts(boolean isSeeMore) {
        List<Product> products = homeDao.getListNewProducts();
        List<ProductCardResponse> response = new ArrayList<>();
        for (Product product : products) {
            String thumbnail = imageDAO.getThumbnail(product.getId());
            int reviewCount = productCardServices.getReviewCount(product.getId());
            int rating = productCardServices.calculateStar(product.getId());
            ProductCardResponse productCardResponse = productMapper.toProductCardResponse(product);
            productCardResponse.setThumbnail(cloudinaryUploadServices.getImage("product_img", thumbnail));
            productCardResponse.setRating(rating);
            productCardResponse.setReviewCount(reviewCount);
            response.add(productCardResponse);
        }
        return response;
    }

    public List<Slider> getListSlideShow() {
        return homeDao.getListSlideShow();
    }

    public Page<ProductCardResponse> getListTrendProducts() {
        return this.getListTrendProducts(Pageable.unpaged());
    }

    public Page<ProductCardResponse> getListTrendProducts(Pageable pageable) {
        List<Product> products = homeDao.getListTrendProducts(pageable.getPageSize(), pageable.getOffset());
        List<ProductCardResponse> response = new ArrayList<>();
        for (Product product : products) {
            String thumbnail = imageDAO.getThumbnail(product.getId());
            int reviewCount = productCardServices.getReviewCount(product.getId());
            int rating = productCardServices.calculateStar(product.getId());
            ProductCardResponse productCardResponse = productMapper.toProductCardResponse(product);
            productCardResponse.setThumbnail(cloudinaryUploadServices.getImage("product_img", thumbnail));
            productCardResponse.setRating(rating);
            productCardResponse.setReviewCount(reviewCount);
            response.add(productCardResponse);
        }
        return new PageImpl(response, pageable, homeDao.countTrendProducts());
    }
}
