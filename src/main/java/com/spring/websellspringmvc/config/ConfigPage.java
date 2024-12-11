package com.spring.websellspringmvc.config;

import org.springframework.stereotype.Component;

@Component
public class ConfigPage {
    public static String CONTACT;
    //    Auth
    public static String SIGN_IN, SIGN_UP, RESET_PASSWORD, VERIFY_SUCCESS, FORGET_PASSWORD;
    //    User
    public static String USER_ACCOUNT, USER_CHANGE_PASSWORD, USER_PURCHASE_HISTORY, USER_CART, CHECKOUT, USER_SUCCESS_ORDER, USER_REVIEW, USER_REVIEW_SUCCESS;
    //    Product
    public static String HOME, PRODUCT_BUYING, PRODUCT_DETAIL, PRODUCT_ORDER, PRODUCT_NEW, PRODUCT_TRENDING;
    //    Admin/Product
    public static String ADMIN_PRODUCT, ADMIN_CATEGORY, ADMIN_ORDER, ADMIN_REVIEW, ADMIN_USER, ADMIN_MATERIAL, DASHBOARD;

    public static String ERROR_404;

    static {
        HOME = "/home";
        CONTACT = "/contact";

//        Config path /auth/
        SIGN_IN = "/auth/signIn";
        SIGN_UP = "/auth/signUp";
        RESET_PASSWORD = "/auth/resetPassword";
        FORGET_PASSWORD = "/auth/forgotPassword";
        VERIFY_SUCCESS = "/auth/verifySuccess";

//        Config path /user
        USER_ACCOUNT = "/user/account";
        USER_CHANGE_PASSWORD = "/user/changePassword";
        USER_PURCHASE_HISTORY = "/user/purchaseHistory";
        USER_CART = "/user/shoppingCart";
        CHECKOUT = "/user/checkout";
        USER_SUCCESS_ORDER = "/user/successOrder";
        USER_REVIEW = "/user/review";
        USER_REVIEW_SUCCESS = "/user/reviewSuccess";

//        Config path /product/
        PRODUCT_BUYING = "/product/productBuying";
        PRODUCT_DETAIL = "/product/productDetail";
        PRODUCT_ORDER = "/product/productOrder";
        PRODUCT_TRENDING = "/product/productTrending";
        PRODUCT_NEW = "/product/productNew";

//        Config path /admin/
        ADMIN_CATEGORY = "/admin/adminCategories";
        ADMIN_ORDER = "/admin/adminOrders";
        ADMIN_PRODUCT = "/admin/adminProducts";
        ADMIN_REVIEW = "/admin/adminReviews";
        ADMIN_USER = "/admin/adminUsers";
        ADMIN_MATERIAL = "/admin/adminImportMaterial";
        DASHBOARD = "/admin/adminDashboard";

        ERROR_404 = "/error/error404";
    }

}
