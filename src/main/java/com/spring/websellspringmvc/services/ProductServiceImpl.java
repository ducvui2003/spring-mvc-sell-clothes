package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dao.*;
import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.dto.mvc.response.ProductDetailResponse;
import com.spring.websellspringmvc.dto.request.DatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.ProductDetailAdminResponse;
import com.spring.websellspringmvc.dto.response.datatable.ProductDatatable;
import com.spring.websellspringmvc.mapper.ProductMapper;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import com.spring.websellspringmvc.utils.constraint.KeyAttribute;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductDAO productDAO;
    ImageDAO imageDAO;
    HomeDAO homeDAO;
    ColorDAO colorDAO;
    SizeDAO sizeDAO;
    ProductMapper productMapper = ProductMapper.INSTANCE;
    CloudinaryUploadServices cloudinaryUploadServices;
    ReviewService reviewService;
    ProductCardDAO productCardDAO;

    @Override
    public Page<ProductCardResponse> getListNewProducts(Pageable pageable) {
        List<Product> products = homeDAO.getListNewProducts();
        List<ProductCardResponse> response = new ArrayList<>();
        for (Product product : products) {
            ProductCardResponse productCardResponse = productMapper.toProductCardResponse(product);
            setRating(productCardResponse);
            setThumbnail(productCardResponse);
            response.add(productCardResponse);
        }
        return new PageImpl(response, pageable, homeDAO.countTrendProducts());
    }

    @Override
    public List<Slider> getListSlideShow() {
        return homeDAO.getListSlideShow();
    }

    @Override
    public Page<ProductCardResponse> getListTrendProducts(Pageable pageable) {
        List<Product> products = homeDAO.getListTrendProducts(pageable.getPageSize(), pageable.getOffset());
        List<ProductCardResponse> response = new ArrayList<>();
        for (Product product : products) {
            ProductCardResponse productCardResponse = productMapper.toProductCardResponse(product);
            setRating(productCardResponse);
            setThumbnail(productCardResponse);
            response.add(productCardResponse);
        }
        return new PageImpl(response, pageable, homeDAO.countTrendProducts());
    }


    @Override
    public ProductDetailResponse getProductDetail(int productId) {
        Product product = productDAO.getProductByProductId(productId);
        if (product == null)
            return null;

        ProductDetailResponse productDetailResponse = productMapper.toProductDetailResponse(product);

        List<String> images = imageDAO.getNameImages(product.getId());
        List<String> urlImage = cloudinaryUploadServices.getImages(ImagePath.PRODUCT.getPath(), images);
        productDetailResponse.setImages(urlImage);

        setRating(productDetailResponse);

        List<ProductDetailResponse.Size> sizes = sizeDAO.getListSizeByProductId(product.getId()).stream().map(size -> ProductDetailResponse.Size.builder().name(size.getNameSize()).price(size.getSizePrice()).id(size.getId()).build()).collect(Collectors.toList());
        productDetailResponse.setSizes(sizes);

        List<ProductDetailResponse.Color> colors = colorDAO.getListColorByProductId(product.getId()).stream().map(color -> ProductDetailResponse.Color.builder().code(color.getCodeColor()).id(color.getId()).build()).collect(Collectors.toList());
        productDetailResponse.setColors(colors);

        return productDetailResponse;
    }

    @Override
    public Page<ProductCardResponse> filter(ProductFilter productFilter) {
        List<Product> products = productDAO.filter(productFilter);
        List<ProductCardResponse> response = new ArrayList<>();
        for (Product product : products) {
            ProductCardResponse productCardResponse = productMapper.toProductCardResponse(product);
            setRating(productCardResponse);
            setThumbnail(productCardResponse);
            response.add(productCardResponse);
        }
        long total = productDAO.countFilter(productFilter);
        return new PageImpl<>(response, productFilter.getPageable(), total);
    }

    @Override
    public DatatableResponse<ProductDatatable> datatable(DatatableRequest datatableRequest) {
        List<ProductDatatable> products = productDAO.datatable(datatableRequest);
        long total = productDAO.datatableCount(datatableRequest);
        return DatatableResponse.<ProductDatatable>builder()
                .data(products)
                .recordsTotal(total)
                .recordsFiltered(total)
                .draw(datatableRequest.getDraw())
                .build();
    }

    @Override
    public void changeVisibility(int productId, boolean visibility) {
        productDAO.updateVisibility(productId, visibility);
    }

    @Override
    public ProductDetailAdminResponse getProductDetailAdmin(int productId) {
        Product product = productDAO.getProductByProductId(productId);
        if (product == null)
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

        List<Size> sizeList = sizeDAO.getListSizeByProductId(productId);
        List<Color> colorList = colorDAO.getListColorByProductId(productId);
        List<Image> imageList = imageDAO.getListImageByProductId(productId);

        imageList.forEach(image -> image.setNameImage(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), image.getNameImage())));
        ProductDetailAdminResponse response = productMapper.toProductDetailAdminResponse(product);

        response.setSizes(sizeList);
        response.setColors(colorList);
        response.setImages(imageList);
        return response;
    }

    private void setRating(ProductCardResponse productCardResponse) {
        Map<String, Object> calculateRating = reviewService.calculateRating(productCardResponse.getId());
        int reviewCount = 0, ratingStar = 0;
        if (calculateRating != null) {
            reviewCount = calculateRating.get(KeyAttribute.REVIEW_COUNT.name()) != null ? (int) calculateRating.get(KeyAttribute.REVIEW_COUNT.name()) : 0;
            ratingStar = calculateRating.get(KeyAttribute.RATING_STAR.name()) != null ? (int) calculateRating.get(KeyAttribute.RATING_STAR.name()) : 0;
        }
        productCardResponse.setRating(ratingStar);
        productCardResponse.setReviewCount(reviewCount);
    }

    private void setRating(ProductDetailResponse productDetailResponse) {
        Map<String, Object> calculateRating = reviewService.calculateRating(productDetailResponse.getId());
        int reviewCount = 0, ratingStar = 0;
        if (calculateRating != null) {
            reviewCount = calculateRating.get(KeyAttribute.REVIEW_COUNT.name()) != null ? (int) calculateRating.get(KeyAttribute.REVIEW_COUNT.name()) : 0;
            ratingStar = calculateRating.get(KeyAttribute.RATING_STAR.name()) != null ? (int) calculateRating.get(KeyAttribute.RATING_STAR.name()) : 0;
        }
        productDetailResponse.setRating(ratingStar);
        productDetailResponse.setReviewCount(reviewCount);
    }

    private void setThumbnail(ProductCardResponse productCardResponse) {
        String thumbnail = imageDAO.getThumbnail(productCardResponse.getId());
        productCardResponse.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), thumbnail));
    }

    public List<Product> getAllProductSelect() {
        return productCardDAO.getProduct();
    }
}

