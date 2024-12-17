package com.spring.websellspringmvc.passkey.config;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RelyingPartyConfiguration {
    @NonFinal
    @Value("${app.service.yubikey.id}")
    String relyingPartyId;
    @NonFinal
    @Value("${app.service.yubikey.name}")
    String relyingPartyName;
    CredentialRepository credentialRepository;
    @Value("#{'${app.service.yubikey.origins}'.split(',')}")
    Set<String> origins;

    @Bean
    public RelyingPartyIdentity relyingPartyIdentity() {
        return RelyingPartyIdentity.builder()
                .id(relyingPartyId)
                .name(relyingPartyName)
                .build();
    }

    @Bean
    public RelyingParty relyingParty(RelyingPartyIdentity relyingPartyIdentity) {
        return RelyingParty.builder()
                .identity(relyingPartyIdentity)
                .credentialRepository(credentialRepository) // Your credential repository
                .origins(origins) // Trusted origins
                .build();
    }
}
