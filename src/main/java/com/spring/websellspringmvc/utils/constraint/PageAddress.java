package com.spring.websellspringmvc.utils.constraint;

import lombok.Getter;

@Getter
public enum PageAddress {
    HOME("/index"),
    PRODUCT_TRENDING("/product/productTrending"),
    PRODUCT_BUYING("/product/productBuying"),
    PRODUCT_NEW("/product/productNew"),
    PRODUCT_DETAIL("/product/productDetail"),
    ABOUT("/about"),

    ADMIN_PRODUCT("/admin/adminProducts"),
    ;

    private final String page;

    PageAddress(String page) {
        this.page = page;
    }

}
