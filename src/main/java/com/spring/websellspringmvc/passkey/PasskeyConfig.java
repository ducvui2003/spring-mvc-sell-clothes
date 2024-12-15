package com.spring.websellspringmvc.passkey;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PasskeyConfig {
    DatabaseCredentialRepository databaseCredentialRepository;

    @Bean
    public RelyingPartyIdentity relyingPartyIdentity() {
        return RelyingPartyIdentity.builder()
                .id("localhost")
                .name("Your Style")
                .build();
    }

    @Bean
    public RelyingParty relyingParty(RelyingPartyIdentity relyingPartyIdentity) {
        return RelyingParty.builder()
                .identity(relyingPartyIdentity)
                .credentialRepository(databaseCredentialRepository) // Your credential repository
                .origins(Set.of("http://localhost:8080", "http://127.0.0.1:8080",
                        "http://localhost:8443", "http://127.0.0.1:8443")) // Trusted origins
                .build();
    }
}
