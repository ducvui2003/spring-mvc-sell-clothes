package com.spring.websellspringmvc.models;

public abstract class AbstractModel {
    abstract Log insert(Log log);

    abstract int delete(int id);
}
