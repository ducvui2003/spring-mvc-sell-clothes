package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.DatatableDAO;
import com.spring.websellspringmvc.dto.request.datatable.UserDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.UserDatatable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserServicesImpl implements AdminUserServices {
    DatatableDAO datatableDAO;

    @Override
    public DatatableResponse<UserDatatable> datatable(UserDatatableRequest request) {
        List<UserDatatable> data = datatableDAO.datatable(request);
        long total = datatableDAO.count(request);
        return DatatableResponse.<UserDatatable>builder()
                .data(data)
                .recordsTotal(total)
                .recordsFiltered(total)
                .draw(request.getDraw())
                .build();
    }
}
