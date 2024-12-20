package com.spring.websellspringmvc.passkey.dto;

import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RegistrationFinishRequest {
    String name;
    PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> request;
}
