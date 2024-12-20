package com.spring.websellspringmvc.passkey.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.passkey.dto.RegistrationFinishRequest;
import com.spring.websellspringmvc.passkey.dto.RegistrationStartRequest;
import com.spring.websellspringmvc.passkey.model.Credential;
import com.spring.websellspringmvc.passkey.service.RegistrationFidoService;
import com.spring.websellspringmvc.session.SessionManager;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.exception.Base64UrlException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegisterFidoController {
    String START_REG_REQUEST = "start_login_request";
    RegistrationFidoService registrationFidoService;
    SessionManager sessionManager;

    @PostMapping("/webauthn/register/start")
    ResponseEntity<PublicKeyCredentialCreationOptions> startRegistration(HttpSession session)
            throws JsonProcessingException, Base64UrlException {
        User user = sessionManager.getUser();
        if (user == null) throw new RuntimeException("User not found");
        RegistrationStartRequest request = RegistrationStartRequest.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
        PublicKeyCredentialCreationOptions response = this.registrationFidoService.startRegistration(request);
        session.setAttribute(START_REG_REQUEST, response.toJson());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webauthn/register/finish")
    ResponseEntity<ApiResponse<Credential>> finishRegistration(
            @RequestBody RegistrationFinishRequest request, HttpSession session)
            throws RegistrationFailedException, JsonProcessingException {
        PublicKeyCredentialCreationOptions response = PublicKeyCredentialCreationOptions.fromJson(session.getAttribute(START_REG_REQUEST).toString());
        if (response == null) {
            throw new RuntimeException("Cloud Not find the original request");
        }
        var result =
                this.registrationFidoService.finishRegistration(
                        request, response);
        if (result != null)
            return ResponseEntity.ok(new ApiResponse<>(200, "Registration success", result));
        return ResponseEntity.ok(new ApiResponse<>(400, "Registration failed", null));
    }
}
