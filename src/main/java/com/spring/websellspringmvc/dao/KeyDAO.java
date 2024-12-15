package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Key;
import com.spring.websellspringmvc.models.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

@Repository
@RegisterBeanMapper(Key.class)
public interface KeyDAO {
    @SqlUpdate("""
    INSERT INTO `keys` (id, publicKey, keyId, userId, createAt, updateAt ) VALUES (:id, :publicKey, :keyId, :userId, NOW(), NOW())""")
    @GetGeneratedKeys
    Long insert(@BindBean Key key);
}
