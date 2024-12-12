package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.request.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;

import java.util.List;

public interface DatatableDAO {
    List<OrderDatatable> datatable(OrderDatatableRequest request);

    long count(OrderDatatableRequest request);
}
