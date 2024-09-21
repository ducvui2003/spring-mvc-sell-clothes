package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.*;
import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.dto.mvc.response.ProductDetailResponse;
import com.spring.websellspringmvc.dto.request.DatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.ProductDataTable;
import com.spring.websellspringmvc.mapper.ProductMapper;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServicesImpl implements ProductService {
    ProductDAO productDAO;
    ProductCardServices productCardServices;
    ImageDAO imageDAO;
    ColorDAO colorDAO;
    SizeDAO sizeDAO;
    ReviewDAO reviewDAO;
    ProductMapper productMapper = ProductMapper.INSTANCE;
    CloudinaryUploadServices cloudinaryUploadServices;

    ProductCardDAO productCardDAO;

    public List<Image> getListImagesByProductId(int productId) {
        return productDAO.getListImagesByProductId(productId);
    }

    public List<Color> getListColorsByProductId(int productId) {
        return productDAO.getListColorsByProductId(productId);
    }

    public List<Size> getListSizesByProductId(int productId) {
        return productDAO.getListSizesByProductId(productId);
    }

    public double getPriceSizeByName(String nameSize, int productId) {
        return productDAO.getPriceSizeByName(nameSize, productId);
    }

    public Size getSizeByNameSizeWithProductId(String nameSize, int productId) {
        return productDAO.getSizeByNameSizeWithProductId(nameSize, productId);
    }


    public Product getProductByProductId(int productId) {
        return productDAO.getProductByProductId(productId);
    }

    public Color getColorByCodeColorWithProductId(String codeColor, int productId) {
        return productDAO.getColorByCodeColorWithProductId(codeColor, productId);
    }

    public List<Product> getAllProductSelect() {
        return productCardDAO.getProduct();
    }

    @Override
    public ProductDetailResponse getProductDetail(int productId) {
        Product product = productDAO.getProductByProductId(productId);
        if (product == null) {
            return null;
        }
        List<Image> images = imageDAO.getNameImages(product.getId());
        int reviewCount = productCardServices.getReviewCount(product.getId());
        int rating = productCardServices.calculateStar(product.getId());
        ProductDetailResponse productDetailResponse = productMapper.toProductDetailResponse(product);
        List<String> urlImage = cloudinaryUploadServices.getImages("product_img", images.stream().map(Image::getNameImage).collect(Collectors.toList()));
        productDetailResponse.setImages(urlImage);
        productDetailResponse.setRating(rating);
        productDetailResponse.setReviewCount(reviewCount);

        List<ProductDetailResponse.Size> sizes = sizeDAO.findSizeByProductId(product.getId()).stream().map(size -> ProductDetailResponse.Size.builder().name(size.getNameSize()).price(size.getSizePrice()).id(size.getId()).build()).collect(Collectors.toList());
        productDetailResponse.setSizes(sizes);

        List<ProductDetailResponse.Color> colors = colorDAO.findColorByProductId(product.getId()).stream().map(color -> ProductDetailResponse.Color.builder().code(color.getCodeColor()).id(color.getId()).build()).collect(Collectors.toList());
        productDetailResponse.setColors(colors);

        return productDetailResponse;
    }

    @Override
    public Page<ProductCardResponse> filter(ProductFilter productFilter) {
        List<Product> products = productDAO.filter(productFilter);
        List<ProductCardResponse> response = new ArrayList<>();
        for (Product product : products) {
            String thumbnail = imageDAO.getThumbnail(product.getId());
            int reviewCount = getReviewCount(product.getId());
            int rating = calculateStar(product.getId());
            ProductCardResponse productCardResponse = productMapper.toProductCardResponse(product);
            productCardResponse.setThumbnail(cloudinaryUploadServices.getImage("product_img", thumbnail));
            productCardResponse.setRating(rating);
            productCardResponse.setReviewCount(reviewCount);
            response.add(productCardResponse);
        }
        long total = productDAO.countFilter(productFilter);
        return new PageImpl<>(response, productFilter.getPageable(), total);
    }

    private int getReviewCount(int productId) {
        List<Review> list = reviewDAO.getReviewStar(productId);
        if (list.isEmpty()) return 0;
        return list.size();
    }

    private int calculateStar(int productId) {
        List<Review> list = reviewDAO.getReviewStar(productId);
        if (list.isEmpty()) return 0;
        int totalStar = 0;
        for (Review item : list) {
            totalStar += item.getRatingStar();
        }
        return totalStar / list.size();
    }

    @Override
    public DatatableResponse<ProductDataTable> datatable(DatatableRequest datatableRequest) {
        List<ProductDataTable> products = productDAO.datatable(datatableRequest);

        return DatatableResponse.<ProductDataTable>builder()
                .data(products)
                .recordsTotal(productDAO.datatableCount(datatableRequest))
                .recordsFiltered(productDAO.datatableCount(datatableRequest))
                .draw(datatableRequest.getDraw())
                .build();
    }
}

