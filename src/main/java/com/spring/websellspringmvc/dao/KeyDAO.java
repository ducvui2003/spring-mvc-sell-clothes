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
            INSERT INTO `keys` (id, publicKey, previousId, userId, createAt, updateAt ) VALUES (:id, :publicKey, :previousId, :userId, NOW(), NOW())""")
    @GetGeneratedKeys
    Long insert(@BindBean Key key);


    @SqlQuery("SELECT * FROM `keys` WHERE userId = :userId ORDER BY createAt DESC")
    List<Key> getKeys(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT * FROM `keys` WHERE userId = :userId AND isDelete = 0 ORDER BY createAt DESC LIMIT 1;
            """)
    Key getCurrentKey(@Bind("userId") int userId);

    @SqlUpdate("""
                      UPDATE `users` SET isBlockKey = 1 , keyOTP = :otp WHERE id = :userId;
            """)
    void deleteCurrentKey(@Bind("userId") int userId, @Bind("otp") String otp);

    @SqlQuery("""
            SELECT Exists(SELECT COUNT(*) FROM `users` WHERE id = :userId AND isBlock = 1);
            """)
    boolean isBlockKey(int userId);
}
