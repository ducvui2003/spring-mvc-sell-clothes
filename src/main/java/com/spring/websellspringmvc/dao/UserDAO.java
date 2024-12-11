package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
@RegisterBeanMapper(User.class)
public interface UserDAO {

    @SqlQuery("SELECT id, username, fullName, gender, phone, email, birthday, isVerify, role, avatar FROM users WHERE id = :id")
    User selectById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM users WHERE username = :username AND isVerify = :isVerify")
    User findByUsername(@Bind("username") String username, @Bind("isVerify") boolean isVerify);

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Bind("email") String email);

    @SqlQuery("SELECT id FROM users WHERE email = :email AND isVerify = :isVerify")
    Optional<User> findByEmail(@Bind("email") String email, @Bind("isVerify") boolean isVerify);

    @SqlUpdate("UPDATE users SET passwordEncoding = :passwordEncoding WHERE id = :id AND passwordEncoding = :passwordEncoding")
    void updatePasswordEncoding(@Bind("id") int id, @Bind("passwordEncoding") String passwordEncoding);

    @SqlQuery("SELECT id, tokenVerifyTime, tokenVerify FROM users WHERE username = ? AND isVerify = 0")
    Optional<User> selectTokenVerify(String username);

    @SqlUpdate("UPDATE users SET isVerify = :isVerify, tokenVerify = :tokenVerify, tokenVerifyTime = :tokenVerifyTime WHERE id = :id")
    void updateTokenVerify(@Bind("id") int id, @Bind("isVerify") boolean isVerify, @Bind("tokenVerify") String tokenVerify, @Bind("tokenVerifyTime") Timestamp timeTokenExpired);

    @SqlQuery("SELECT id, tokenResetPassword, tokenResetPasswordTime FROM users WHERE email = :email")
    List<User> selectTokenResetPassword(@Bind("email") String email);

    @SqlUpdate("UPDATE users SET tokenResetPassword = :tokenResetPassword, tokenResetPasswordTime = :tokenResetPasswordTime WHERE id = :id")
    void updateTokenResetPassword(@Bind(":id") int id, @Bind(":tokenResetPassword") String tokenResetPassword, @Bind("tokenResetPasswordTime") Timestamp tokenResetPasswordTime);

    @SqlUpdate("""
            INSERT INTO users (username, passwordEncoding, fullName, gender, email, phone, birthDay, isVerify, role, tokenVerifyTime, tokenVerify) 
            VALUES ( :username, :passwordEncoding, :fullName, :gender, :email, :phone, :birthDay, :verify, :role, :tokenVerifyTime, :tokenVerify)
            """)
    @GetGeneratedKeys
    Long insert(@BindBean User user);

    @SqlUpdate("UPDATE users SET fullName = :fullName, gender = :gender, phone = :phone, birthDay = :birthDay WHERE id = :id")
    void updateUserById(@Bind("id") int id, @Bind("fullName") String fullName, @Bind("gender") String gender, @Bind("phone") String phone, @Bind("birthDay") Date birthDay);

    @SqlUpdate("UPDATE users SET fullName = :fullName, gender = :gender, phone = :phone, birthDay = :birthDay, role = :role WHERE id = :id")
    public void updateUser(@BindBean User user);

    @SqlUpdate("UPDATE users SET passwordEncoding = :password WHERE id = :id")
    public void updateUserPassword(@Bind("id") int userId, @Bind("password") String password);

    @SqlUpdate("UPDATE users SET avatar = :avatar WHERE id = :id")
    public void updateInfoUser(@Bind("id") int id, @Bind("avatar") String avatar);

    @SqlQuery("""
            SELECT DISTINCT users.id, users.fullName 
            FROM users JOIN (orders JOIN order_details ON orders.id = order_details.orderId) ON users.id = orders.userId 
            WHERE order_details.id = :orderDetailId
            """)
    public List<User> getUserByIdProductDetail(@Bind("orderDetailId") int orderDetailId);

    @SqlQuery("SELECT COUNT(*) count FROM users")
    public long getQuantity();

    @SqlQuery("Select id, username, email, fullName, gender, phone, birthDay, role from users limit :limit offset :offset")
    public List<User> getLimit(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery("""
            SELECT id, username, fullName, gender, email, phone, birthday, role " + "FROM users 
            WHERE username LIKE :search OR fullName LIKE :search OR email LIKE :search OR phone LIKE :search 
            ORDER BY :orderBy :orderDir LIMIT :limit OFFSET :start"
            """)
    public List<User> selectWithCondition(Integer start, Integer length, String searchValue, String orderBy, String orderDir);

    @SqlQuery("SELECT COUNT(*) count FROM users WHERE username LIKE :search OR fullName LIKE %:search% OR email LIKE :search OR phone LIKE :search")
    public long getSizeWithCondition(@Bind("searchValue") String search);

}
