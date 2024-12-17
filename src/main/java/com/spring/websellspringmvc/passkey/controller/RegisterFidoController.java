package com.spring.websellspringmvc.passkey.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.websellspringmvc.passkey.dto.RegistrationStartRequest;
import com.spring.websellspringmvc.passkey.service.RegistrationFidoService;
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

    @PostMapping("/webauthn/register/start")
    ResponseEntity<PublicKeyCredentialCreationOptions> startRegistration(
            @RequestBody RegistrationStartRequest request, HttpSession session)
            throws JsonProcessingException, Base64UrlException {
        PublicKeyCredentialCreationOptions response = this.registrationFidoService.startRegistration(request);
        session.setAttribute(START_REG_REQUEST, response.toJson());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webauthn/register/finish")
    ResponseEntity<Boolean> finishRegistration(
            @RequestBody PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> request, HttpSession session)
            throws RegistrationFailedException, JsonProcessingException {
        PublicKeyCredentialCreationOptions response = PublicKeyCredentialCreationOptions.fromJson(session.getAttribute(START_REG_REQUEST).toString());
        if (response == null) {
            throw new RuntimeException("Cloud Not find the original request");
        }
        var result =
                this.registrationFidoService.finishRegistration(
                        request, response);
        return ResponseEntity.ok(result);
    }
}
