package com.spring.websellspringmvc.controller.api.admin;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.request.OrderStatusChangeRequest;
import com.spring.websellspringmvc.dto.request.datatable.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.StatusChangedResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.services.admin.AdminOrderServices;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {
    AdminOrderServices orderServices;

    @PostMapping("/datatable")
    public ResponseEntity<DatatableResponse<OrderDatatable>> datatable(@RequestBody OrderDatatableRequest requestBody) {
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

    @GetMapping("/status-target/{orderId}")
    public ResponseEntity<ApiResponse<StatusChangedResponse>> getStatusTarget(@PathVariable("orderId") String orderId) {
        StatusChangedResponse data = orderServices.getStatusCanChanged(orderId);

        return ResponseEntity.ok(ApiResponse.<StatusChangedResponse>builder()
                .message("Get status target success")
                .data(data)
                .build());
    }

    @PutMapping("/status-target/{orderId}")
    public ResponseEntity<ApiResponse<?>> changeStatus(@PathVariable("orderId") String orderId, @RequestBody OrderStatusChangeRequest request) {
        boolean changedSuccess = orderServices.changeStatus(orderId, request);
        if (changedSuccess) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Change status failed")
                    .build());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.builder()
                .message("Change status failed")
                .build());
    }
}
