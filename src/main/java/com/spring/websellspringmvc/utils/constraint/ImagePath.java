package com.spring.websellspringmvc.utils.constraint;

public enum ImagePath {
    CATEGORY("size_table"),
    PARAMETER("parameter_guide"),
    PRODUCT("product_img"),
    USER ("user");

    private final String path;

    ImagePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
