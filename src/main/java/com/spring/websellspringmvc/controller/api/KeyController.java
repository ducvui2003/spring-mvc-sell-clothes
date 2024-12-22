package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.response.KeyResponse;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.key.KeyServices;
import com.spring.websellspringmvc.services.mail.IMailServices;
import com.spring.websellspringmvc.services.mail.MailLostKey;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.Token;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/key")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyController {
    SessionManager sessionManager;
    KeyServices keyService;

    @PostMapping("/add-key")
    public ResponseEntity<ApiResponse<?>> addKey(@RequestParam("inputUploadKey") MultipartFile request) throws IOException {
        User user = sessionManager.getUser();
        if (request == null || request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Key is empty")
                    .build());
        }

        String keyPairAlgorithm = null;
        String secureRandom = null;
        String provider = null;
        String signature = null;
        int keySize = 0;
        String publicKey = null;

        try (DataInputStream reader = new DataInputStream(new DataInputStream(request.getInputStream()))) {
            keyPairAlgorithm = reader.readUTF();
            try {
                secureRandom = reader.readUTF();
                provider = reader.readUTF();
                signature = reader.readUTF();
                keySize = Integer.parseInt(reader.readUTF());
                publicKey = reader.readUTF();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<String>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Private key file is invalid or corrupted!")
                        .build());
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Invalid key size format")
                    .build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error reading the file")
                    .build());
        }
        int userID = sessionManager.getUser().getId();
        keyService.insertKey(publicKey, userID);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(HttpServletResponse.SC_OK)
                .message("Key added successfully")
                .data(publicKey.toString())
                .build());
    }

    @PutMapping("/lost-key")
    public ResponseEntity<ApiResponse<?>> lostKey() {
        User user = sessionManager.getUser();
        String otp = Token.generateOTPCode();
        keyService.setInvalidKey(user.getId(), otp);
        IMailServices mailServices = new MailLostKey(user.getEmail(), user.getUsername(), otp);
        try {
            mailServices.send();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(HttpServletResponse.SC_OK)
                .message("Key blocked successfully")
                .build());
    }

    @PutMapping("/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOTP(@RequestBody Map<String, String> inp) {
        User user = sessionManager.getUser();
        String otp = inp.get("inputPassword");
        if (otp == null || otp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("OTP is empty")
                    .build());
        }
        if (otp.length() != 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Invalid OTP")
                    .build());
        }
        boolean valid = keyService.setValidKey(user.getId(), otp);
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .code(HttpServletResponse.SC_OK)
                .message("Key verified successfully")
                .data(valid)
                .build());
    }

    @GetMapping("/is-blocking")
    public ResponseEntity<ApiResponse<Boolean>> isBlocking(@RequestParam("orderId") String uuid) {
        User user = sessionManager.getUser();
        boolean isBlock = keyService.isBlockKey(user.getId(), uuid);
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .code(HttpServletResponse.SC_OK)
                .message("Key is blocking")
                .data(isBlock)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<KeyResponse>>> getAll() {
        int userId = sessionManager.getUser().getId();
        List<KeyResponse> keyList = keyService.getKeys(userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpServletResponse.SC_OK, "Get keys", keyList));
    }

    @GetMapping("/download-exe")
    public ResponseEntity<Resource> downloadExeFile() {
        try {
            // Load the .exe file from resources
            Resource resource = new ClassPathResource("static/mysetup.exe");

            // Set headers to force download
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"mysetup.exe\"")
                    .body(resource);
        } catch (Exception ex) {
            throw new RuntimeException("Error while downloading file: " + ex.getMessage());
        }
    }
}
