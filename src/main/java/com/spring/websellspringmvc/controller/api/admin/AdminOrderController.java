package com.spring.websellspringmvc.controller.api.admin;

import com.spring.websellspringmvc.dto.request.DatatableRequest;
import com.spring.websellspringmvc.dto.request.OrderDatatableRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.datatable.OrderDatatable;
import com.spring.websellspringmvc.dto.response.datatable.ProductDatatable;
import com.spring.websellspringmvc.services.OrderServices;
import com.spring.websellspringmvc.services.OrderServicesImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {
    OrderServices orderServices;

    @PostMapping("/datatable")
    public ResponseEntity<DatatableResponse<OrderDatatable>> processRequest(@RequestBody OrderDatatableRequest requestBody) throws ServletException, IOException {
        DatatableResponse<OrderDatatable> response = orderServices.datatable(requestBody);
        return ResponseEntity.ok(response);
    }
}
