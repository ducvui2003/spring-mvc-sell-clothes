package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dao.KeyDAO;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.models.Key;
import com.spring.websellspringmvc.services.admin.AdminOrderServices;
import com.spring.websellspringmvc.services.order.OrderServices;
import com.spring.websellspringmvc.services.pdf.PDFService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    SessionManager sessionManager;
    OrderServices orderServices;
    SignedOrderFile signedOrderFile;
    AdminOrderServices adminOrderServices;
    KeyDAO keyDAO;
    PDFService pdfService;

    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("orderId") String orderId) {
        int userId = sessionManager.getUser().getId();

        // Retrieve order details and previous orders
        OrderDetailResponse orderDetailResponse = orderServices.getOrderByOrderId(orderId, userId);
        List<AdminOrderDetailResponse> orderPrevious = adminOrderServices.getOrderPrevious(orderId);

        if (orderPrevious == null) {
            return;
        }

        // Generate signature from order details
        String signature = signedOrderFile.generateSignature(orderDetailResponse, orderPrevious);

        // Create temporary file from uploaded multipart file
        File tempFile = signedOrderFile.createTempFile(multipartFile);

        try {
            // Read signature from the uploaded PDF
            String uploadSignature = pdfService.readSignature(tempFile);

            // Verify signature and update order status if valid
            if (uploadSignature.equals(signature)) {
                orderServices.updateOrderStatusVerify(orderId, userId);
            }
        } finally {
            // Ensure temporary file is deleted
            tempFile.delete();
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
        String hash;
        try {
            hash = signedOrderFile.hashData(orderDetailResponse, orderPrevious);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] file = pdfService.createDataFile(orderDetailResponse, orderPrevious, hash);
//        byte[] file = signedOrderFile.writeDateFile(orderDetailResponse, orderPrevious);
        String LOCATION = "Downloads";
        String fileName = uuid + ".pdf";
        try {
            FileOutputStream stream = new FileOutputStream(System.getenv("USERPROFILE") + File.separator + LOCATION + File.separator + fileName);
            ServletOutputStream out = res.getOutputStream();
            res.setContentType("application/pdf");
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
