package com.spring.websellspringmvc.controller.api.admin;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.request.CreateUserRequest;
import com.spring.websellspringmvc.dto.request.datatable.DatatableRequest;
import com.spring.websellspringmvc.dto.request.UpdateUserRequest;
import com.spring.websellspringmvc.dto.request.datatable.UserDatatableRequest;
import com.spring.websellspringmvc.dto.response.AdminUserDetailResponse;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.dto.response.datatable.UserDatatable;
import com.spring.websellspringmvc.mapper.UserMapper;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.admin.AdminUserServices;
import com.spring.websellspringmvc.services.user.UserServicesImpl;
import com.spring.websellspringmvc.utils.Encoding;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {
    UserMapper userMapper = UserMapper.INSTANCE;
    UserServicesImpl userServicesImpl;
    AdminUserServices adminUserServices;

    @PostMapping("/datatable")
    public ResponseEntity<DatatableResponse<UserDatatable>> getDatatable(@RequestBody UserDatatableRequest request) {
        DatatableResponse<UserDatatable> response = adminUserServices.datatable(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminUserDetailResponse>> getUser(@PathVariable("id") int id) {
        AdminUserDetailResponse response = adminUserServices.getDetail(id);
        if (response == null) {
            return ResponseEntity.ok(ApiResponse.<AdminUserDetailResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Failed")
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.<AdminUserDetailResponse>builder()
                .code(200)
                .message("Success")
                .data(response)
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
