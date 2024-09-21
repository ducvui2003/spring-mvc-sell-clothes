package com.spring.websellspringmvc.config;

import org.springframework.stereotype.Component;

@Component
public class ConfigPage {
    public static String CONTACT;
    //    Auth
    public static String SIGN_IN, SIGN_UP, RESET_PASSWORD, VERIFY_SUCCESS, FORGET_PASSWORD;
    //    User
    public static String USER_ACCOUNT, USER_CHANGE_PASSWORD, USER_PURCHASE_HISTORY, USER_CART, USER_CHECKOUT, USER_SUCCESS_ORDER, USER_REVIEW, USER_REVIEW_SUCCESS;
    //    Product
    public static String HOME, PRODUCT_BUYING, PRODUCT_DETAIL, PRODUCT_ORDER, PRODUCT_NEW, PRODUCT_TRENDING;
    //    Admin/Product
    public static String ADMIN_PRODUCT, ADMIN_CATEGORY, ADMIN_ORDER, ADMIN_REVIEW, ADMIN_USER, ADMIN_MATERIAL, DASHBOARD;

    public static String ERROR_404;

    static {
        HOME = "/home";
        CONTACT = "/contact";
//        Config path /public/auth/
        String folderAuth = "/auth/";
        SIGN_IN = folderAuth + "signIn";
        SIGN_UP = folderAuth + "signUp";
        RESET_PASSWORD = folderAuth + "resetPassword";
        FORGET_PASSWORD = folderAuth + "forgotPassword";
        VERIFY_SUCCESS = folderAuth + "verifySuccess";

//        Config path /public/user
        String folderUser = "/user/";
        USER_ACCOUNT = folderUser + "account";
        USER_CHANGE_PASSWORD = folderUser + "changePassword";
        USER_PURCHASE_HISTORY = folderUser + "purchaseHistory";
        USER_CART = folderUser + "shoppingCart";
        USER_CHECKOUT = folderUser + "checkout";
        USER_SUCCESS_ORDER = folderUser + "successOrder";
        USER_REVIEW = folderUser + "review";
        USER_REVIEW_SUCCESS = folderUser + "reviewSuccess";

//        Config path /public/product/
        String folderProduct = "/product/";
        PRODUCT_BUYING = folderProduct + "productBuying";
        PRODUCT_DETAIL = folderProduct + "productDetail";
        PRODUCT_ORDER = folderProduct + "productOrder";
        PRODUCT_TRENDING = folderProduct + "productTrending";
        PRODUCT_NEW = folderProduct + "productNew";

//        Config path /public/admin/
//        Config path /public/admin/product/
        String folderAdmin = "/admin/";
        ADMIN_CATEGORY = folderAdmin + "adminCategories";
        ADMIN_ORDER = folderAdmin + "adminOrders";
        ADMIN_PRODUCT = folderAdmin + "adminProducts";
        ADMIN_REVIEW = folderAdmin + "adminReviews";
        ADMIN_USER = folderAdmin + "adminUsers";
        ADMIN_MATERIAL = folderAdmin + "adminImportMaterial";
        DASHBOARD = folderAdmin + "adminDashboard";

        String folderError = "/error/";
        ERROR_404 = folderError + "error404";
    }

}
