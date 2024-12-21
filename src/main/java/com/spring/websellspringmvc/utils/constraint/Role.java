package com.spring.websellspringmvc.utils.constraint;

public enum Role {
    USER,
    ADMIN;


    @Override
    public String toString() {
        return this.name();
    }
}
