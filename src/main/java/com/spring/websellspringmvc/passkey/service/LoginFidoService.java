package com.spring.websellspringmvc.passkey.service;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dao.CartDAO;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.passkey.dto.LoginStartRequest;
import com.spring.websellspringmvc.services.user.UserServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.exception.Base64UrlException;
import com.yubico.webauthn.exception.AssertionFailedException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LoginFidoService {
    UserServices userServices;
    RelyingParty relyingParty;
    SessionManager sessionManager;
    CartDAO cartDAO;

    public AssertionRequest startLogin(LoginStartRequest loginStartRequest) throws Base64UrlException {

        Optional<User> userOptional = userServices.findByEmail(loginStartRequest.getEmail(), true);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
        if (user.getUserHandle() == null)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        System.out.println(user.getEmail());
        StartAssertionOptions options =
                StartAssertionOptions.builder()
                        .timeout(60_000)
                        .username(loginStartRequest.getEmail())
//                        .userHandle(ByteArray.fromBase64Url(user.getUserHandle()))
                        .build();
        AssertionRequest assertionRequest = this.relyingParty.startAssertion(options);

        return assertionRequest;
    }

    public void finishLogin(PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> loginFinishRequest, AssertionRequest assertionRequest)
            throws AssertionFailedException {

        FinishAssertionOptions options =
                FinishAssertionOptions.builder()
                        .request(assertionRequest)
                        .response(loginFinishRequest)
                        .build();

        AssertionResult assertionResult = this.relyingParty.finishAssertion(options);

        if (!assertionResult.isSuccess())
            throw new AppException(ErrorCode.UNAUTHORIZED);
        String email = assertionRequest.getUsername().get();
        Optional<User> userOptional = userServices.findByEmail(email, true);
        User user = userOptional.get();
        int quantityCart = cartDAO.getQuantityCart(user.getId());
        sessionManager.addUser(user);
        sessionManager.setQuantityCart(quantityCart);
    }
}
