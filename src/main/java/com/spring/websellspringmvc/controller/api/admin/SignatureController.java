package com.spring.websellspringmvc.controller.api.admin;

import com.spring.websellspringmvc.dto.request.SignatureRequest;
import com.spring.websellspringmvc.dto.response.SignatureResponse;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class SignatureController {
    @Value("${app.service.cloudinary.cloud-name}")
    private String cloudName;
    @Value("${app.service.cloudinary.api-key}")
    private String apiKey;
    CloudinaryUploadServices cloudinaryUploadServices;

    @PostMapping("/signature")
    public ResponseEntity<SignatureResponse> signature(@RequestBody SignatureRequest request) {
        Map<String, Object> paramsToSign = new HashMap<>();
        List<SignatureResponse.Sign> signs = new ArrayList<>(request.getNumberOfFiles());
        String timestamp;
        for (int i = 0; i < signs.size(); i++) {
            timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            paramsToSign.put("public_id", request.getPublicId()[i]);
            paramsToSign.put("folder", request.getFolder()[i]);
            paramsToSign.put("timestamp", timestamp);

            SignatureResponse.Sign sign = new SignatureResponse.Sign();
            try {
                String signature = cloudinaryUploadServices.generateSignature(paramsToSign);
                sign.setSignature(signature);
                sign.setTimestamp(timestamp);
            } catch (Exception e) {
                sign.setError("Fail to generate signature");
            }
            signs.add(sign);
        }
        return ResponseEntity.ok(SignatureResponse.builder()
                .apiKey(apiKey)
                .cloudName(cloudName)
                .signs(signs)
                .build());
    }
}
