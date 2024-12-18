package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dao.KeyDAO;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.models.Key;
import com.spring.websellspringmvc.services.admin.AdminOrderServices;
import com.spring.websellspringmvc.services.checkout.CheckoutServices;
import com.spring.websellspringmvc.services.order.OrderServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.SignedOrderFile;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/verify-order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadDownloadController {
    CheckoutServices checkoutServices;
    SessionManager sessionManager;
    OrderServices orderServices;
    SignedOrderFile signedOrderFile;
    AdminOrderServices adminOrderServices;
    KeyDAO keyDAO;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Boolean>> uploadFile(@RequestParam("uuid") String uuid, @RequestParam("signed") String signed) {
        try {
            int userId = sessionManager.getUser().getId();
            OrderDetailResponse orderDetailResponse = orderServices.getOrderByOrderId(uuid, userId);
            List<AdminOrderDetailResponse> orderPrevious = adminOrderServices.getOrderPrevious(uuid);
            byte[] fileData = signedOrderFile.writeDateFile(orderDetailResponse, orderPrevious);
            if (fileData == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<Boolean>builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Error verifying signed order")
                        .data(false)
                        .build());
            }
            List<Key> keys = keyDAO.getKeys(userId);
            PublicKey k = KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(keys.get(0).getPublicKey())));

            boolean b = signedOrderFile.verifyData(fileData, signed, k);
            return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                    .code(HttpStatus.OK.value())
                    .message(b ? "Success verifying signed order" : "Error verifying signed order")
                    .data(b)
                    .build());
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam("uuid") String uuid, HttpServletResponse res) {
        int userId = sessionManager.getUser().getId();
        OrderDetailResponse orderDetailResponse = orderServices.getOrderByOrderId(uuid, userId);
        List<AdminOrderDetailResponse> orderPrevious = adminOrderServices.getOrderPrevious(uuid);
        if (orderPrevious == null) {
            return;
        }
        byte[] file = signedOrderFile.writeDateFile(orderDetailResponse, orderPrevious);
        String LOCATION = "Downloads";
        String fileName = uuid + ".data";
        try {
            FileOutputStream stream = new FileOutputStream(System.getenv("USERPROFILE") + File.separator + LOCATION + File.separator + fileName);
            ServletOutputStream out = res.getOutputStream();
            res.setContentType("application/data");
            res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            res.setContentLength(file.length);
            for (byte b : file) {
                out.write(b);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            return;
        } finally {
        }
    }

}
