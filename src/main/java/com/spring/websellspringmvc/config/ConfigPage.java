package com.spring.websellspringmvc.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

//@WebListener
public class ConfigPage implements ServletContextListener {
    public static String DOMAIN;

    public static String CONTACT;
    //    Auth
    public static String SIGN_IN, SIGN_UP, RESET_PASSWORD, VERIFY, FORGET_PASSWORD;
    //    User
    public static String USER_ACCOUNT, USER_CHANGE_PASSWORD, USER_PURCHASE_HISTORY, USER_CART, USER_CHECKOUT, USER_SUCCESS_ORDER, USER_REVIEW, USER_REVIEW_SUCCESS;
    //    Product
    public static String HOME, PRODUCT_BUYING, PRODUCT_DETAIL, PRODUCT_ORDER, PRODUCT_NEW, PRODUCT_TRENDING;
    //    Admin/Product
    public static String ADMIN_PRODUCT, ADMIN_CATEGORY, ADMIN_ORDER, ADMIN_REVIEW, ADMIN_USER, ADMIN_MATERIAL, DASHBOARD;

    public static String ERROR_404;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        ServletContext servletContext = sce.getServletContext();
        DOMAIN = servletContext.getInitParameter("contextPath");
        init();
    }

    private void init() {
        HOME = "/index";
        CONTACT = "/contact";
//        Config path /public/auth/
        String folderAuth = "";
        SIGN_IN = folderAuth + "signIn";
        SIGN_UP = folderAuth + "signUp";
        RESET_PASSWORD = folderAuth + "resetPassword";
        FORGET_PASSWORD = folderAuth + "forgotPassword";
        VERIFY = folderAuth + "verifySuccess";

//        Config path /public/user
        String folderUser = "/user";
        USER_ACCOUNT = folderUser + "account";
        USER_CHANGE_PASSWORD = folderUser + "changePassword";
        USER_PURCHASE_HISTORY = folderUser + "purchaseHistory";
        USER_CART = folderUser + "shoppingCart";
        USER_CHECKOUT = folderUser + "checkout";
        USER_SUCCESS_ORDER = folderUser + "successOrder";
        USER_REVIEW = folderUser + "review";
        USER_REVIEW_SUCCESS = folderUser + "reviewSuccess";

//        Config path /public/product/
        String folderProduct = "/product";
        PRODUCT_BUYING = folderProduct + "productBuying";
        PRODUCT_DETAIL = folderProduct + "productDetail";
        PRODUCT_ORDER = folderProduct + "productOrder";
        PRODUCT_TRENDING = folderProduct + "productTrending";
        PRODUCT_NEW = folderProduct + "productNew";

//        Config path /public/admin/
//        Config path /public/admin/product/
        String folderAdminProduct = "/admin/product/";
        ADMIN_CATEGORY = folderAdminProduct + "adminCategories";
        ADMIN_ORDER = folderAdminProduct + "adminOrders";
        ADMIN_PRODUCT = folderAdminProduct + "adminProducts";
        ADMIN_REVIEW = folderAdminProduct + "adminReviews";
        ADMIN_USER = folderAdminProduct + "adminUsers";
        ADMIN_MATERIAL = folderAdminProduct + "adminImportMaterial";
        DASHBOARD = folderAdminProduct + "adminDashboard";

        String folderError = "/error/";
        ERROR_404 = folderError + "error404";
    }

}
