package com.spring.websellspringmvc.controller.api.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.dto.VoucherDetailRequest;
import com.spring.websellspringmvc.dto.request.CreateVoucherRequest;
import com.spring.websellspringmvc.dto.request.DatatableRequest;
import com.spring.websellspringmvc.dto.request.UpdateVoucherRequest;
import com.spring.websellspringmvc.dto.request.VisibleVoucherRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.mapper.VoucherMapper;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.ProductServiceImpl;
import com.spring.websellspringmvc.services.voucher.VoucherServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/voucher")
public class AdminVoucherController {
    VoucherServices voucherServices;
    VoucherMapper voucherMapper;
    ProductServiceImpl productServiceImpl;

    @PostMapping("/datatable")
    public ResponseEntity<DatatableResponse<Voucher>> getDatatable(@RequestBody DatatableRequest request) {
        // Mapping order column index to database column name
        String[] columnNames = {"code", "availableTurns", "createAt", "createAt", "state"};
        String orderBy = request.getOrderColumn() < columnNames.length ? columnNames[request.getOrderColumn()] : columnNames[0];
        // Fetch filtered and sorted logs
        List<Voucher> vouchers = voucherServices.getVoucher(request.getStart(), request.getLength(), request.getSearchValue(), orderBy, request.getOrderDir());
        long size = voucherServices.getTotalWithCondition(request.getSearchValue());

        return ResponseEntity.ok(DatatableResponse.<Voucher>builder()
                .draw(request.getDraw())
                .recordsTotal(size)
                .recordsFiltered(size)
                .data(vouchers)
                .build());
    }

    @PostMapping("/detail")
    public ResponseEntity<VoucherDetailRequest> getDetail(@RequestParam("code") String code) {
        if (code == null)
            return ResponseEntity.badRequest().build();

        VoucherDetailRequest voucherDetailRequest = voucherServices.getDetail(code);
        return ResponseEntity.ok(voucherDetailRequest);
    }

    @PostMapping("/visible")
    public ResponseEntity<?> changeState(@RequestBody VisibleVoucherRequest request) {
        JsonObject jsonObject = new JsonObject();
        try {
            voucherServices.changeState(request.getCode(), request.getState());
            jsonObject.addProperty("success", true);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (IllegalArgumentException e) {
            jsonObject.addProperty("success", false);
            return ResponseEntity.badRequest().body(jsonObject.toString());
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createVoucher(@RequestBody CreateVoucherRequest request) {
        JsonObject jsonObject = new JsonObject();
        try {
            Voucher voucher = voucherMapper.toVoucher(request);
            boolean saveSuccess = voucherServices.saveVoucher(voucher, Arrays.asList(request.getProductId()));
            jsonObject.addProperty("success", saveSuccess);
        } catch (Exception e) {
            jsonObject.addProperty("success", false);
        }
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/get-product")
    public ResponseEntity<?> getProduct() {
        JsonArray jsonArray = new JsonArray();
        List<Product> listProduct = productServiceImpl.getAllProductSelect();
        for (Product product : listProduct) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", product.getId());
            jsonObject.addProperty("name", product.getName());
            jsonArray.add(jsonObject);
        }
        return ResponseEntity.ok(jsonArray.toString());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateVoucher(@RequestBody UpdateVoucherRequest request) {
        JsonObject jsonObject = new JsonObject();
        try {
            Voucher voucher = voucherMapper.toVoucher(request);
            List<Integer> listProductId = Arrays.asList(request.getProductId());
            boolean saveSuccess = voucherServices.updateVoucher(voucher, listProductId);
            jsonObject.addProperty("success", saveSuccess);
        } catch (Exception e) {
            jsonObject.addProperty("success", false);
        }
        return ResponseEntity.ok(jsonObject.toString());
    }
}
