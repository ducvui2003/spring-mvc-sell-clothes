package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dto.request.datatable.UserDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.UserDatatable;

public interface AdminUserServices {
    DatatableResponse<UserDatatable> datatable(UserDatatableRequest request);

}
