package com.spring.websellspringmvc.passkey.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorView;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.passkey.dto.RegistrationFinishRequest;
import com.spring.websellspringmvc.passkey.model.Credential;
import com.spring.websellspringmvc.passkey.dto.RegistrationStartRequest;
import com.spring.websellspringmvc.services.user.UserServices;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.data.exception.Base64UrlException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationFidoService {
    RelyingParty relyingParty;
    UserServices userServices;
    PasskeyService passkeyService;

    public PublicKeyCredentialCreationOptions startRegistration(RegistrationStartRequest startRequest) throws Base64UrlException {
        Optional<User> userOptional = userServices.findByEmail(startRequest.getEmail(), true);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorView.ERROR_404));

        if (user.getUserHandle() == null) {
            byte[] userHandle = generateUserHandle();
            String userHandleBase64 = new ByteArray(userHandle).getBase64Url();
            user.setUserHandle(userHandleBase64);
            userServices.updateUserHandle(user.getId(), userHandleBase64);
        }
        PublicKeyCredentialCreationOptions options = createPublicKeyCredentialCreationOptions(user);
        return options;
    }


    private PublicKeyCredentialCreationOptions createPublicKeyCredentialCreationOptions(
            User user) throws Base64UrlException {
        var userIdentity =
                UserIdentity.builder()
                        .name(user.getEmail())
                        .displayName(user.getUsername())
                        .id(ByteArray.fromBase64Url(user.getUserHandle()))
                        .build();

        var authenticatorSelectionCriteria =
                AuthenticatorSelectionCriteria.builder()
                        .userVerification(UserVerificationRequirement.DISCOURAGED)
                        .build();

        var startRegistrationOptions =
                StartRegistrationOptions.builder()
                        .user(userIdentity)
                        .timeout(30_000)
                        .authenticatorSelection(authenticatorSelectionCriteria)
                        .build();

        PublicKeyCredentialCreationOptions options =
                this.relyingParty.startRegistration(startRegistrationOptions);

        return options;
    }

    public Credential finishRegistration(
            RegistrationFinishRequest finishRequest,
            PublicKeyCredentialCreationOptions credentialCreationOptions)
            throws RegistrationFailedException, JsonProcessingException {

        FinishRegistrationOptions options =
                FinishRegistrationOptions.builder()
                        .request(credentialCreationOptions)
                        .response(finishRequest.getRequest())
                        .build();
        RegistrationResult registrationResult = this.relyingParty.finishRegistration(options);

//        User user = userServices.findByEmail(credentialCreationOptions.getUser().getName(), true)
//                .orElseThrow(() -> new AppException(ErrorView.ERROR_404));

        Credential credential =
                Credential.builder()
                        .id(registrationResult.getKeyId().getId().getBase64Url()) // credentialId
                        .name(finishRequest.getName())
                        .publicKey(registrationResult.getPublicKeyCose().getBase64Url())
                        .userId(credentialCreationOptions.getUser().getId().getBase64Url())
                        .type(registrationResult.getKeyId().getType().name())
                        .signCount(registrationResult.getSignatureCount())
                        .build();

        this.passkeyService.addCredential(credential);

        return credential;
    }

    private byte[] generateUserHandle() {
        return UUID.randomUUID().toString().getBytes();
    }
}
