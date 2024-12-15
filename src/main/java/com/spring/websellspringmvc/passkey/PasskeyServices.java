package com.spring.websellspringmvc.passkey;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import com.yubico.webauthn.data.PublicKeyCredentialEntity;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PasskeyServices {
    RelyingParty relyingParty;
    DatabaseCredentialRepository databaseCredentialRepository;

    public PublicKeyCredentialCreationOptions startRegistration(String username, ByteArray userHandle) {
        // Prepare user verification options and challenge
        UserIdentity userEntity = UserIdentity.builder()
                .name(username)              // The username
                .displayName(username)       // Display name for the user (can be the same as username)
                .id(userHandle)              // The unique user handle (e.g., a byte array representing the user)
                .build();

        PublicKeyCredentialCreationOptions options = relyingParty.startRegistration(
                StartRegistrationOptions.builder().user(userEntity)
                        .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                                .residentKey(ResidentKeyRequirement.REQUIRED)
                                .build())
                        .build()
        );
        return options;
    }

}
