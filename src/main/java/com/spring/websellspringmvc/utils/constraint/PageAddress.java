package com.spring.websellspringmvc.utils.constraint;

import lombok.Getter;

@Getter
public enum PageAddress {
    HOME("home"),
    PRODUCT_TRENDING("/product/productTrending"),
    PRODUCT_BUYING("/product/productBuying"),
    CHECKOUT("checkout"),
    LOGIN("login"),
    REGISTER("register"),
    PROFILE("profile"),
    ORDER("order"),
    SEARCH("search"),
    ERROR("error");

    private final String page;

    PageAddress(String page) {
        this.page = page;
    }

}
