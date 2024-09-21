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
    @NonFinal
    static final int LIMIT = 9;
    ProductCardDAO productCardDAO;
    ReviewDAO reviewDAO;
    ProductDAO productDAO;
    SizeDAO sizeDAO;
    ImageDAO imageDAO;
    ColorDAO colorDAO;
    CategoryDAO categoryDAO;
    ProductMapper productMapper = ProductMapper.INSTANCE;
    CloudinaryUploadServices cloudinaryUploadServices;

    public List<Category> getAllCategory() {
        return categoryDAO.getAllCategory();
    }

    public List<Color> getAllColor() {
        return colorDAO.getAllColor();
    }

    public List<Size> getAllSize() {
        return sizeDAO.getAllSize();
    }

    public List<Product> getProducts(int numberPage) {
        List<Product> productCardList = productCardDAO.getProducts(numberPage, LIMIT, true);
        return productCardList;
    }

    public int getQuantityPage() {
        double quantityPage = Math.ceil(Double.parseDouble(productCardDAO.getQuantityProduct(true) + "") / LIMIT);
        return (int) quantityPage;
    }

    public int getQuantityPage(List<Integer> listId) {
        double quantityPage = Math.ceil(Double.parseDouble(productCardDAO.getQuantityProduct(listId, true) + "") / LIMIT);
        return (int) quantityPage;
    }

    public List<Product> filter(List<Integer> listId, int pageNumber) {
        int offset = (pageNumber - 1) * LIMIT;
        return productCardDAO.pagingAndFilter(listId, offset, LIMIT, true);
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

    public int getReviewCount(int productId) {
        List<Review> list = reviewDAO.getReviewStar(productId);
        if (list.isEmpty()) return 0;
        return list.size();
    }

    public int calculateStar(int productId) {
        List<Review> list = reviewDAO.getReviewStar(productId);
        if (list.isEmpty()) return 0;
        int totalStar = 0;
        for (Review item : list) {
            totalStar += item.getRatingStar();
        }
        return totalStar / list.size();
    }

    public List<Product> getProductByCategoryId(int categoryId, int quantity, boolean isRandom) {
        List<Product> productList = productCardDAO.getProductByCategoryId(categoryId);
        List<Product> result = new ArrayList<>();
        if ((productList.size() - quantity) < 10) {
            for (int i = 0; i < quantity; i++) {
                try {
                    result.add(productList.get(i));
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
            return result;
        }
        int numRd = 0;
        Random rd = new Random();
        for (int i = 0; i < quantity; i++) {
            Product p = null;
            if (isRandom) {
                numRd = rd.nextInt(productList.size());
                p = productList.get(numRd);
                while (productList.contains(p)) {
                    numRd = rd.nextInt(productList.size());
                    System.out.println(rd);
                    p = productList.get(numRd);
                }
            } else {
                p = productList.get(i);
            }
            result.add(p);
        }
        return result;
    }

    public String getNameCategoryById(int id) {
        return productCardDAO.getNameCategoryById(id).get(0).getNameType();
    }

    public List<Parameter> getParameterByIdCategory(int id) {
        return productCardDAO.getParametersByProductId(id);
    }

    public Category getCategoryById(int id) {
        return productCardDAO.getCategoryByProductId(id).get(0);
    }

    public String getNameProductByIdOrderDetail(int orderDetailId) {
        return reviewDAO.getNameProductByOrderDetailId(orderDetailId).get(0).getName();
    }

    public String getNameProductById(int productId) {
        return productCardDAO.getNameProductById(productId).get(0).getName();
    }
}
