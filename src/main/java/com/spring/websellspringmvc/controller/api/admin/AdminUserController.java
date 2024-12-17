package com.spring.websellspringmvc.controller.api.admin;

import com.spring.websellspringmvc.dto.request.CreateUserRequest;
import com.spring.websellspringmvc.dto.request.datatable.DatatableRequest;
import com.spring.websellspringmvc.dto.request.UpdateUserRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.mapper.UserMapper;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.user.UserServicesImpl;
import com.spring.websellspringmvc.utils.Encoding;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {
    UserMapper userMapper = UserMapper.INSTANCE;
    UserServicesImpl userServicesImpl;

    @PostMapping("/datatable")
    public ResponseEntity<DatatableResponse<User>> getDatatable(@RequestBody DatatableRequest request) {
        // Mapping order column index to database column name
        String[] columnNames = {"id", "username", "email", "fullName", "gender"};
        String orderBy = request.getOrderColumn() < columnNames.length ? columnNames[request.getOrderColumn()] : columnNames[0];
        // Fetch filtered and sorted logs
        List<User> users = userServicesImpl.getUser(request.getStart(), request.getLength(), request.getSearchValue(), orderBy, request.getOrderDir());
        long size = userServicesImpl.getTotalWithCondition(request.getSearchValue());

        return ResponseEntity.ok(DatatableResponse.<User>builder()
                .draw(request.getDraw())
                .recordsTotal(size)
                .recordsFiltered(size)
                .data(users)
                .build());
    }

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody CreateUserRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            User user = userMapper.toUser(request);
            user.setPasswordEncoding(Encoding.getINSTANCE().toSHA1(request.getPassword()));
            userServicesImpl.insertUser(user);
            jsonObject.put("success", true);
        } catch (Exception e) {
            jsonObject.put("success", false);
        }
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody UpdateUserRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            User user = userMapper.toUser(request);
            userServicesImpl.updateUser(user);
            jsonObject.put("success", true);
        } catch (Exception e) {
            jsonObject.put("success", false);
        }
        return ResponseEntity.ok(jsonObject.toString());
    }
}
