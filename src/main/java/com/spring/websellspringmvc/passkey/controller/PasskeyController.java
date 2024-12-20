package com.spring.websellspringmvc.passkey.controller;

import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.passkey.model.Credential;
import com.spring.websellspringmvc.passkey.service.PasskeyService;
import com.spring.websellspringmvc.session.SessionManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passkey")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PasskeyController {
    PasskeyService passkeyService;
    SessionManager sessionManager;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Credential>>> getCredential() {
        int userId = sessionManager.getUser().getId();
        return ResponseEntity.ok(ApiResponse.<List<Credential>>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(passkeyService.getCredential(userId))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCredential(
            @PathVariable("id") String credentialId) {
        int userId = sessionManager.getUser().getId();
        passkeyService.deleteCredential(credentialId, userId);
        return ResponseEntity.ok(ApiResponse.<List<Credential>>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .build());
    }
}
