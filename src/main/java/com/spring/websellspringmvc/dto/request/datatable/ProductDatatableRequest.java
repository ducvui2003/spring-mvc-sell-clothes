package com.spring.websellspringmvc.dto.request.datatable;

public class ProductDatatableRequest extends DatatableRequest {
    int[] categoryId;
    int[] keyword;
    String search;
    Double moneyStart;
    Double moneyEnd;
}
