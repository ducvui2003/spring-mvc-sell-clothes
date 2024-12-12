package com.spring.websellspringmvc.controller.api.admin;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.request.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.services.AdminOrderServices;
import jakarta.servlet.ServletException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {
    AdminOrderServices orderServices;

    @PostMapping("/datatable")
    public ResponseEntity<DatatableResponse<OrderDatatable>> processRequest(@RequestBody OrderDatatableRequest requestBody) throws ServletException, IOException {
        DatatableResponse<OrderDatatable> response = orderServices.datatable(requestBody);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminOrderDetailResponse>> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<AdminOrderDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get order detail success")
                .data(orderServices.getOrderDetail(id)).build());
    }
}
