package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.passkey.model.Credential;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredentialDAO {
    @SqlUpdate("INSERT INTO users_credentials (id, name, public_key, sign_count, user_id, type) VALUES (:id, :name, :publicKey, :signCount, :userId, :type)")
    void addCredential(@BindBean Credential credential);


    @SqlQuery("""
            SELECT users_credentials.* FROM users_credentials JOIN users ON users_credentials.user_id = users.userHandle
            WHERE users.id = :userId
            """)
    @RegisterBeanMapper(Credential.class)
    List<Credential> getCredential(@Bind("userId") Integer userId);

    @SqlUpdate("""
            DELETE users_credentials FROM users_credentials JOIN users ON users_credentials.user_id = users.userHandle 
            WHERE users_credentials.id = :credentialId AND users.id = :userId
            """)
    int deleteCredential(@Bind("credentialId") String credentialId,
                         @Bind("userId") int userId);
}
