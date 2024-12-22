package com.spring.websellspringmvc.passkey.service;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dao.CredentialDAO;
import com.spring.websellspringmvc.passkey.model.Credential;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PasskeyService {
    CredentialDAO credentialDAO;

    public List<Credential> getCredential(Integer userId) {
        return credentialDAO.getCredential(userId);
    }

    public void addCredential(Credential credential) {
        credentialDAO.addCredential(credential);
    }

    public void deleteCredential(String credentialId, int userId) {
        int rowEffect = credentialDAO.deleteCredential(credentialId, userId);
        if (rowEffect == 0) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }
}
