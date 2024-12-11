package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dto.request.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.dto.response.datatable.ProductDatatable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderServicesImpl implements OrderServices {
    OrderDAO orderDAO;

    @Override
    public DatatableResponse<OrderDatatable> datatable(OrderDatatableRequest request) {
        List<OrderDatatable> products = orderDAO.datatable(request);
        long total = orderDAO.datatableCount(request);
        return DatatableResponse.<OrderDatatable>builder()
                .data(products)
                .recordsTotal(total)
                .recordsFiltered(total)
                .draw(request.getDraw())
                .build();
    }
}
