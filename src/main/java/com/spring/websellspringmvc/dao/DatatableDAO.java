package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.request.datatable.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.request.datatable.UserDatatableRequest;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.dto.response.datatable.UserDatatable;

import java.util.List;

public interface DatatableDAO {
    List<OrderDatatable> datatable(OrderDatatableRequest request);

    long count(OrderDatatableRequest request);

    List<UserDatatable> datatable(UserDatatableRequest request);

    long count(UserDatatableRequest request);
}
