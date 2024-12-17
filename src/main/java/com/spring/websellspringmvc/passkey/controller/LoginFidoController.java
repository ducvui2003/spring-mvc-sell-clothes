package com.spring.websellspringmvc.passkey.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.passkey.dto.LoginStartRequest;
import com.spring.websellspringmvc.passkey.service.LoginFidoService;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.exception.Base64UrlException;
import com.yubico.webauthn.exception.AssertionFailedException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LoginFidoController {
    String START_REG_REQUEST = "start_login_request";
    LoginFidoService loginFidoService;

    @PostMapping("/webauthn/login/start")
    ResponseEntity<AssertionRequest> loginStartResponse(
            @RequestBody LoginStartRequest request, HttpSession session) throws JsonProcessingException, Base64UrlException {
        AssertionRequest response = this.loginFidoService.startLogin(request);
        session.setAttribute(START_REG_REQUEST, response.toJson());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webauthn/login/finish")
    ResponseEntity<ApiResponse<?>> loginStartResponse(@RequestBody PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> request, HttpSession session)
            throws AssertionFailedException, JsonProcessingException {
        AssertionRequest assertionRequest = AssertionRequest.fromJson(session.getAttribute(START_REG_REQUEST).toString());
        if (assertionRequest == null) {
            throw new RuntimeException("Cloud Not find the original request");
        }

        this.loginFidoService.finishLogin(request, assertionRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "Login success", Map.of()));
    }
}
