package com.spring.websellspringmvc.utils.constraint;

import lombok.Getter;

@Getter
public enum PageAddress {
    HOME("/index"),
    SIGN_IN("/auth/signIn"),
    SIGN_UP("/auth/signUp"),
    PRODUCT_TRENDING("/product/productTrending"),
    PRODUCT_BUYING("/product/productBuying"),
    PRODUCT_NEW("/product/productNew"),
    PRODUCT_DETAIL("/product/productDetail"),
    ABOUT("/about"),
    USER_INFO("/user/accountInfo"),
    USER_SECURITY("/user/accountSecurity"),
    USER_ORDER("/user/accountOrder"),
    USER_KEY("/user/accountKey"),
    ADMIN_ORDER("/admin/adminOrders"),
    ADMIN_USER("/admin/adminUsers"),
    ADMIN_REVIEW("/admin/adminReviews"),
    ADMIN_PRODUCT("/admin/adminProducts"),
    ADMIN_CATEGORY("/admin/adminCategories"),
    ADMIN_VOUCHER("/admin/adminVoucher"),
    ADMIN_DASHBOARD("/admin/adminDashboard"),
    ERROR_404("/error/404"),
    FORGET_PASSWORD("/auth/forgotPassword"),
    USER_CART("/user/shoppingCart"),
    VERIFY_SUCCESS("/auth/verifySuccess"),
    RESET_PASSWORD("/auth/resetPassword"),
    CHECKOUT("/user/checkout"),
    USER_REVIEW_SUCCESS("/user/reviewSuccess"),
    USER_REVIEW("/user/review");

    private final String page;

    PageAddress(String page) {
        this.page = page;
    }

}
