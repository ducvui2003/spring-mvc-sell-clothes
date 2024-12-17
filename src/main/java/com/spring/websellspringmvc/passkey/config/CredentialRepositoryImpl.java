package com.spring.websellspringmvc.passkey.config;

import com.spring.websellspringmvc.passkey.model.Credential;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.PublicKeyCredentialType;
import com.yubico.webauthn.data.exception.Base64UrlException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CredentialRepositoryImpl implements CredentialRepository {
    Jdbi jdbi;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String email) {
        return new HashSet<>(jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT id 
                                FROM users_credentials 
                                WHERE user_id = (SELECT userHandle FROM users WHERE email = :email)
                                """)
                        .bind("email", email)
                        .map((rs, ctx) -> {
                            try {
                                return PublicKeyCredentialDescriptor.builder()
                                        .id(ByteArray.fromBase64Url(rs.getString("id")))
                                        .type(PublicKeyCredentialType.PUBLIC_KEY)
                                        .build();
                            } catch (Base64UrlException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toSet())));
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String email) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT userHandle FROM users WHERE email = :email
                                """)
                        .bind("email", email)
                        .mapTo(String.class)
                        .findOne()
                        .map(userId -> {
                            try {
                                return ByteArray.fromBase64Url(userId);
                            } catch (Base64UrlException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }) // Convert user ID to ByteArray
        );
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        Optional<String> userName = jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT email 
                                FROM users 
                                WHERE userHandle = :userHandle
                                """)
                        .bind("userHandle", userHandle.getBase64Url())
                        .mapTo(String.class)
                        .findOne());
        return userName;
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        // userId = userHandle
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT c.id, c.public_key, c.sign_count, c.user_id
                                   FROM users_credentials c 
                                   WHERE c.id = :credentialId AND c.user_id = :userHandle
                                """)
                        .bind("credentialId", credentialId.getBase64Url())
                        .bind("userHandle", userHandle.getBase64Url())
                        .map((rs, ctx) -> {
                            try {
                                return RegisteredCredential.builder()
                                        .credentialId(credentialId)
                                        .userHandle(userHandle)
                                        .publicKeyCose(ByteArray.fromBase64Url(rs.getString("public_key")))
                                        .signatureCount(rs.getLong("sign_count"))
                                        .build();
                            } catch (Base64UrlException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        })
                        .findOne());
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray userHandle) {
        List<Credential> credentials = jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT c.id, c.public_key, c.sign_count, c.user_id 
                                FROM users_credentials c 
                                WHERE c.user_id = :user_id
                                """)
                        .bind("user_id", userHandle.getBase64Url()).mapToBean(Credential.class).list()
        );
        return credentials.stream()
                .map(credential -> {
                            try {
                                return RegisteredCredential.builder()
                                        .credentialId(ByteArray.fromBase64Url(credential.getId()))
                                        .userHandle(userHandle)
                                        .publicKeyCose(ByteArray.fromBase64Url(credential.getPublicKey()))
                                        .signatureCount(credential.getSignCount())
                                        .build();
                            } catch (Base64UrlException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                )
                .collect(Collectors.toSet());
    }
}
