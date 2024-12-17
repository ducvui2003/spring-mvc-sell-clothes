package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Key;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RegisterBeanMapper(Key.class)
public interface KeyDAO {
    @SqlUpdate("""
    INSERT INTO `keys` (id, publicKey, keyId, userId, createAt, updateAt ) VALUES (:id, :publicKey, :keyId, :userId, NOW(), NOW())""")
    @GetGeneratedKeys
    Long insert(@BindBean Key key);

    @SqlQuery("SELECT * FROM `keys` WHERE userId = :userId ORDER BY createAt DESC")
    List<Key> getKeys(@Bind("userId") int userId);

    @SqlUpdate("""
        update `keys` set isDelete = 1 where  userId = :userId AND isDelete = 0;""")
    void deleteCurrentKey(@Bind("userId") int userId);
}
