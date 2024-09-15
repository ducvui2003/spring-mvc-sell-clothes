package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.*;
import com.spring.websellspringmvc.models.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServices {

    ProductDAO productDao;
    ImageDAO imageDAO;
    ColorDAO colorDAO;
    SizeDAO sizeDAO;

    ProductCardDAO productCardDAO;

    public List<Image> getListImagesByProductId(int productId) {
        return productDao.getListImagesByProductId(productId);
    }

    public List<Color> getListColorsByProductId(int productId) {
        return productDao.getListColorsByProductId(productId);
    }

    public List<Size> getListSizesByProductId(int productId) {
        return productDao.getListSizesByProductId(productId);
    }

    public double getPriceSizeByName(String nameSize, int productId) {
        return productDao.getPriceSizeByName(nameSize, productId);
    }

    public Size getSizeByNameSizeWithProductId(String nameSize, int productId) {
        return productDao.getSizeByNameSizeWithProductId(nameSize, productId);
    }


    public Product getProductByProductId(int productId) {
        return productDao.getProductByProductId(productId);
    }

    public Color getColorByCodeColorWithProductId(String codeColor, int productId) {
        return productDao.getColorByCodeColorWithProductId(codeColor, productId);
    }

    public List<Product> getAllProductSelect() {
        return productCardDAO.getProduct();
    }
}

