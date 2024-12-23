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


    @SqlQuery("""
            SELECT id as id, publicKey as publicKey, previousId as previousId, userId as userId, createAt as createAt, updateAt as updateAt, deleted as deleted FROM `keys` WHERE userId = :userId ORDER BY createAt DESC""")
    List<Key> getKeys(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT * FROM `keys` WHERE userId = :userId AND deleted = 0 ORDER BY createAt DESC LIMIT 1;
            """)
    Key getCurrentKey(@Bind("userId") int userId);

    @SqlUpdate("""
                      UPDATE `users` SET isBlockKey = 1 , keyOTP = :otp, blockKeyAt = NOW() WHERE id = :userId;
            """)
    void blockKey(@Bind("userId") int userId, @Bind("otp") String otp);

    @SqlUpdate("""
                      UPDATE `keys` SET deleted = 1 WHERE userId = :userId;
            """)
    void deleteKey(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT Count(*) FROM `users` u\s
                              JOIN (
                                  SELECT userId, previousId, orderStatusId, createAt
                                  FROM (
                                       SELECT userId,previousId, orderStatusId, createAt,
                                           ROW_NUMBER() OVER (
                                           PARTITION BY previousId
                                           ORDER BY
                                              CASE orderStatusId
                                                  WHEN 6 THEN 1
                                                  WHEN 7 THEN 1
                                                  WHEN 1 THEN 2
                                                  WHEN 2 THEN 3
                                                  ELSE 4
                                                  END DESC,
                                                  createAt ASC ) AS row_num
                                       FROM orders o\s
                                       WHERE o.previousId NOT IN (
                                           SELECT DISTINCT previousId\s
                                           FROM orders\s
                                           WHERE orderStatusId IN (3,4,5)
                                       )
                                        AND previousId LIKE  ':uuid' ) AS subquery
                                          WHERE row_num = 1
                              ) o ON u.id = o.userId
                          WHERE u.id = :userId AND u.isBlockKey = 1 AND o.previousId = ':uuid'
                          AND o.createAt < u.blockKeyAt
            """)
    int isBlockKey(@Bind("userId") int userId, @Bind("uuid") String uuid);


    @SqlUpdate("""
                      UPDATE `users` SET isBlockKey = 0 , keyOTP = NULL, blockKeyAt = '2022-12-31 23:59:59' WHERE id = :userId AND keyOTP = :otp;
            """)
    boolean unblockKey(int userId, String otp);


    @SqlQuery("""
            SELECT id, publicKey, previousId, userId, createAt, updateAt, deleted 
            FROM `keys`
            WHERE userId = :userId
            ORDER BY deleted ASC, createAt DESC
            """)

    public List<Key> getAllKeys(@Bind("userId") int userId);
}
