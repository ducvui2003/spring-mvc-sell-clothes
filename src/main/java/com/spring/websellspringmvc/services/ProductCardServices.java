package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.*;
import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.mapper.ProductMapper;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.MoneyRange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCardServices {
    ProductCardDAO productCardDAO;
    ReviewDAO reviewDAO;
    SizeDAO sizeDAO;
    ColorDAO colorDAO;
    CategoryDAO categoryDAO;

    public List<Category> getAllCategory() {
        return categoryDAO.getAllCategory();
    }

    public List<Color> getAllColor() {
        return colorDAO.getAllColor();
    }

    public List<Size> getAllSize() {
        return sizeDAO.getAllSize();
    }

    public List<Integer> getIdProductFromCategoryId(String[] categoryIds) {
        List<Product> listProduct = productCardDAO.getIdProductByCategoryId(Arrays.asList(categoryIds));
        if (listProduct.isEmpty()) return null;
        List<Integer> listId = new ArrayList<>();
        for (Product p : listProduct) {
            listId.add(p.getId());
        }
        return listId;
    }

    public List<Integer> getIdProductFromSize(String[] sizes) {
        List<Product> listProduct = productCardDAO.getIdProductBySize(Arrays.asList(sizes));
        if (listProduct.isEmpty()) return null;
        List<Integer> listId = new ArrayList<>();
        for (Product p :
                listProduct) {
            listId.add(p.getId());
        }
        return listId;
    }

    public List<Integer> getIdProductFromColor(String[] colors) {
        List<Product> listProduct = productCardDAO.getIdProductByColor(Arrays.asList(colors));
        if (listProduct.isEmpty()) return null;
        List<Integer> listId = new ArrayList<>();
        for (Product p :
                listProduct) {
            listId.add(p.getId());
        }
        return listId;
    }

    public List<Integer> getIdProductFromMoneyRange(List<MoneyRange> moneyRangeList) {
        List<Product> listProduct = productCardDAO.getIdProductByMoneyRange(moneyRangeList);
        if (listProduct.isEmpty()) return new ArrayList<>();
        List<Integer> listId = new ArrayList<>();
        for (Product p : listProduct) {
            listId.add(p.getId());
        }
        return listId;
    }


    public List<Parameter> getParameterByIdCategory(int id) {
        return productCardDAO.getParametersByProductId(id);
    }

    public String getNameProductById(int productId) {
        return productCardDAO.getNameProductById(productId).get(0).getName();
    }
}
