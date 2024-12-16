package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.models.Key;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.key.KeyServiceImpl;
import com.spring.websellspringmvc.session.SessionManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/key")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyController {
    SessionManager sessionManager;
    private final KeyServiceImpl keyServiceImpl;

    @PostMapping("/add-key")
    public ResponseEntity<ApiResponse<?>> addKey(@RequestParam("inputUploadKey") MultipartFile inputUploadKey, @RequestParam("currentKeyId") String currentKeyId) throws IOException {
        if (inputUploadKey == null || inputUploadKey.isEmpty()) {
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

        try (DataInputStream reader = new DataInputStream(new DataInputStream(inputUploadKey.getInputStream()))) {
            keyPairAlgorithm = reader.readUTF();
            secureRandom = reader.readUTF();
            provider = reader.readUTF();
            signature = reader.readUTF();
            keySize = Integer.parseInt(reader.readUTF());
            publicKey = reader.readUTF();
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
        keyServiceImpl.insertKey(currentKeyId ,publicKey, userID);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(HttpServletResponse.SC_OK)
                .message("Key added successfully")
                .data(publicKey.toString())
                .build());
    }
}
