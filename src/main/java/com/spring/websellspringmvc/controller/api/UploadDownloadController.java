package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.services.HistoryService;
import com.spring.websellspringmvc.services.cart.CartService;
import com.spring.websellspringmvc.services.cart.CartServiceImpl;
import com.spring.websellspringmvc.services.checkout.CheckoutServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.SignedOrderFile;
import jakarta.mail.Multipart;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/verifyOrder")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadDownloadController {
    CheckoutServices checkoutServices;
    SessionManager sessionManager;
    HistoryService historyService;
    SignedOrderFile signedOrderFile;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("uuid") String uuid, @RequestParam("file") MultipartFile file) {
        try {
            // Save or process the file
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            System.out.println(fileSize);
            OrderDetailResponse orderDetailResponse =signedOrderFile.readDataFile(file.getBytes());
            if (orderDetailResponse == null) {
                return ;
            }
            System.out.println(orderDetailResponse.toString());
            // For example, save the file to a directory
//            Path filePath = Paths.get("uploads/" + fileName);
//            Files.createDirectories(filePath.getParent());
//            Files.write(filePath, file.getBytes());


//            return ResponseEntity.ok("File uploaded successfully with UUID: " + uuid);
        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam("uuid") String uuid, HttpServletResponse res) {
        int userId = sessionManager.getUser().getId();
        OrderDetailResponse orderDetailResponse = historyService.getOrderByOrderId(uuid, userId);

        if (orderDetailResponse == null) {
            return ;
        }
        byte[] file = signedOrderFile.writeDateFile(orderDetailResponse);
        String LOCATION = "Downloads";
        String fileName = uuid + ".data";
        try {
            FileOutputStream stream = new FileOutputStream( System.getenv("USERPROFILE")+ File.separator +LOCATION+ File.separator + fileName);
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
            return ;
        } finally {
        }
    }

}
