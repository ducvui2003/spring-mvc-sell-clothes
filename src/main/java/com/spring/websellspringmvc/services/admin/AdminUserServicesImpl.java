package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.DatatableDAO;
import com.spring.websellspringmvc.dao.KeyDAO;
import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.dto.request.datatable.UserDatatableRequest;
import com.spring.websellspringmvc.dto.response.AdminUserDetailResponse;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.UserDatatable;
import com.spring.websellspringmvc.mapper.UserMapper;
import com.spring.websellspringmvc.models.User;
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
    UserDAO userDAO;
    KeyDAO keyDAO;
    UserMapper userMapper = UserMapper.INSTANCE;

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

    @Override
    public AdminUserDetailResponse getDetail(int userId) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return null;
        }
        AdminUserDetailResponse response = userMapper.toAdminUserDetailResponse(user);
        List
        return response;
    }
}
