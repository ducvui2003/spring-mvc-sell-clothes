package com.spring.websellspringmvc.controller.api.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.controller.exception.ResourceNotFoundException;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.response.DashBoardDetailResponse;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.services.admin.DashboardServices;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashBoardController {
    DashboardServices dashboardServices;

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<DashBoardDetailResponse>> getDetailByMonthAndYear(
            @RequestParam("month") String month,
            @RequestParam("year") String year
    ) {
        if (month.isBlank() || year.isBlank())
            throw new ResourceNotFoundException();

        DashBoardDetailResponse result = dashboardServices.getDashBoardDetail(month, year);
        JsonObject products = new JsonObject();
        JsonArray arrayProductsPopular = new JsonArray();
        JsonArray arrayProductsNotPopular = new JsonArray();
        for (Map.Entry<Product, Integer> entry : result.getProductPopular().entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("id", entry.getKey().getId());
            object.addProperty("name", entry.getKey().getName());
            object.addProperty("quantity", entry.getValue());
            arrayProductsPopular.add(object);
        }
        for (Map.Entry<Product, Integer> entry : result.getProductNotPopular().entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("id", entry.getKey().getId());
            object.addProperty("name", entry.getKey().getName());
            object.addProperty("quantity", entry.getValue());
            arrayProductsNotPopular.add(object);
        }
        products.add("notPopular", arrayProductsNotPopular);
        products.add("popular", arrayProductsPopular);
        products.addProperty("orderSuccess", result.getOrderSuccess());
        products.addProperty("orderFailed", result.getOrderFailed());
        products.addProperty("revenue", result.getRevenue());
        return ResponseEntity.ok(ApiResponse.<DashBoardDetailResponse>builder().data(result).code(HttpStatus.OK.value()).build());
    }
}
