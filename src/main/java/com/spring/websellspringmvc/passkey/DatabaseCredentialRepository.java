package com.spring.websellspringmvc.passkey;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.PublicKeyCredentialType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseCredentialRepository implements CredentialRepository {
    Jdbi jdbi;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        return new HashSet<>(jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT credential_id FROM credentials 
                                WHERE user_id = (SELECT id FROM users WHERE username = :username)
                                """)
                        .bind("username", username)
                        .map((rs, ctx) -> PublicKeyCredentialDescriptor.builder()
                                .id(ByteArray.fromBase64(rs.getString("credential_id")))
                                .type(PublicKeyCredentialType.PUBLIC_KEY)
                                .build())
                        .collect(Collectors.toSet())));
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT id FROM users WHERE username = :username
                                """)
                        .bind("username", username)
                        .mapTo(Integer.class)
                        .findOne()
                        .map(userId -> new ByteArray(userId.toString().getBytes())) // Convert user ID to ByteArray
        );
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT username 
                                FROM users 
                                WHERE id = :userId
                                """)
                        .bind("userId", Integer.parseInt(new String(userHandle.getBytes())))
                        .mapTo(String.class)
                        .findOne()
        );
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        // userId = userHandle
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT c.credential_id, c.public_key, c.sign_count, c.user_id
                                   FROM credentials c
                                   WHERE c.credential_id = :credentialId AND c.user_id = :userId
                                """)
                        .bind("credentialId", credentialId.getBase64Url())
                        .bind("userHandle", userHandle.getBase64())
                        .map((rs, ctx) -> RegisteredCredential.builder()
                                .credentialId(credentialId)
                                .userHandle(userHandle)
                                .publicKeyCose(ByteArray.fromBase64(rs.getString("public_key")))
                                .signatureCount(rs.getLong("sign_count"))
                                .build())
                        .findOne());
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray userHandle) {
        return new HashSet<>(jdbi.withHandle(handle ->
                handle.createQuery("""
                                SELECT c.credential_id, c.public_key, c.sign_count, c.user_id
                                FROM credentials c
                                WHERE c.user_id = :userId
                                """)
                        .bind("userId", Integer.parseInt(new String(userHandle.getBytes())))
                        .map((rs, ctx) -> RegisteredCredential.builder()
                                .credentialId(new ByteArray(rs.getString("credential_id").getBytes()))
                                .userHandle(new ByteArray(rs.getString("user_id").getBytes()))
                                .publicKeyCose(new ByteArray(rs.getString("public_key").getBytes()))
                                .signatureCount(rs.getLong("sign_count"))
                                .build()
                        )
                        .list()
        ));
    }
}
