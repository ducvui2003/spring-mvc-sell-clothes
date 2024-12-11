package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dto.request.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;

public interface OrderServices {
    DatatableResponse<OrderDatatable> datatable(OrderDatatableRequest request);

}
