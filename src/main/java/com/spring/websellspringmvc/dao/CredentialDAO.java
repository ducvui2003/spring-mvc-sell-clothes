package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.passkey.model.Credential;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface CredentialDAO {
    @SqlUpdate("""
            INSERT INTO credential (
            user_id, credential_id, public_key, sign_count, created_at) 
            VALUES(:userId, :credentialId, :publicKey, :signCount, :createAt)
            """)
    void addCredential(@BindBean Credential credential);
}
