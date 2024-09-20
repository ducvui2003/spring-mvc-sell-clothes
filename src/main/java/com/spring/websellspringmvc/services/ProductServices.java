package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.*;
import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.models.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServices {
    ProductDAO productDAO;
    ProductCardServices productCardServices;
    ImageDAO imageDAO;
    ColorDAO colorDAO;
    SizeDAO sizeDAO;

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


}

