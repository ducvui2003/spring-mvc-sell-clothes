package com.spring.websellspringmvc.utils;

import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.admin.AdminReviewServices;
import com.spring.websellspringmvc.services.ProductCardServices;
import com.spring.websellspringmvc.services.ProductServices;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductFactory {
    ProductServices productServices;
    ProductCardServices productCardServices;

    public static Product getProductById(int productId) {
//        return ProductServices.getINSTANCE().getProductByProductId(productId);
        return null;
    }

    public List<Image> getListImagesByProductId(int productId) {
        return productServices.getListImagesByProductId(productId);
    }

    public static List<Color> getListColorsByProductId(int productId) {
//        return ProductServices.getINSTANCE().getListColorsByProductId(productId);
        return null;
    }

    public static List<Size> getListSizesByProductId(int productId) {
//        return ProductServices.getINSTANCE().getListSizesByProductId(productId);
        return null;
    }

    public static double getPriceSizeByName(String nameSize, int productId) {
//        return ProductServices.getINSTANCE().getPriceSizeByName(nameSize, productId);
        return 0;
    }

    public static int getReviewCount(int productId) {
//        return ProductCardServices.getINSTANCE().getReviewCount(productId);
        return 0;
    }

    public static int calculateStar(int productId) {
//        return ProductCardServices.getINSTANCE().calculateStar(productId);
        return 0;
    }

    public static Size getSizeByNameSizeWithProductId(String nameSize, int productId) {
//        return ProductServices.getINSTANCE().getSizeByNameSizeWithProductId(nameSize, productId);
        return null;
    }

    public static Color getColorByCodeColorWithProductId(String codeColor, int productId) {
//        return ProductServices.getINSTANCE().getColorByCodeColorWithProductId(codeColor, productId);
        return null;
    }

    public static String getNameCategoryById(int id) {
//        return ProductCardServices.getINSTANCE().getNameCategoryById(id);
        return null;
    }

    public static User getUserByIdOrderDetail(int orderDetailId) {
//        return AdminReviewServices.getINSTANCE().getUserByIdProductDetail(orderDetailId);
        return null;
    }

    public static String getNameProductByIdOrderDetail(int orderDetailId) {
//        return ProductCardServices.getINSTANCE().getNameProductByIdOrderDetail(orderDetailId);
        return null;
    }

}
