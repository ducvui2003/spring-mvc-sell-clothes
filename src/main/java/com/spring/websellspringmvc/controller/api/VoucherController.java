package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dao.VoucherDAO;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.voucher.VoucherServices;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/voucher")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {
    VoucherDAO voucherDAO;
    VoucherServices voucherServices;

//    @PostMapping("/apply")
//    public ResponseEntity applyVoucher(@RequestBody ApplyVoucherRequest request) {
//        JsonObject jsonObject = new JsonObject();
//        try {
//            User user = SessionManager.getInstance(req, resp).getUser();
//            if (idsParam != null) {
//                List<Integer> ids = List.of(idsParam).stream().map(Integer::parseInt).collect(Collectors.toList());
//                VoucherDetailRequest voucherDetailRequest = VoucherServices.getINSTANCE().canApply(user, code, ids);
//                jsonObject.add("result", gson.toJsonTree(voucherDetailRequest));
//                jsonObject.addProperty("code", 200);
//            } else {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                jsonObject.addProperty("code", 1005);
//            }
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            jsonObject.addProperty("code", 1005);
//        }
//        resp.getWriter().write(gson.toJson(jsonObject));
//    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllVoucher(@RequestParam(value = "id[]", required = true) Integer[] ids) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (ids != null) {
                List<Voucher> vouchers = voucherServices.getAll(Arrays.asList(ids));
                response.put("vouchers", vouchers);
            }
            response.put("success", true);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<Voucher>> getVoucher(@RequestParam(value = "id", required = true) Integer id) {
        List<Voucher> vouchers = voucherServices.getAll();
        return ResponseEntity.ok().body(vouchers);
    }
}
