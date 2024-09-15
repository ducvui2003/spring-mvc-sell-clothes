package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Address;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RegisterBeanMapper(Address.class)
public interface AddressDAO {

    @SqlQuery("SELECT id, province, ward, district, detail FROM address WHERE userId = :userId")
    public List<Address> getAddress(@Bind("userId") int userId);

    @SqlUpdate("""
            INSERT INTO address (userId, province, district, ward, detail) 
            VALUES (:userId, :province, :district, :ward, :detail)
            """)
    @GetGeneratedKeys
    public Integer insertAddress(@BindBean Address address);

    @SqlUpdate("""
            UPDATE address SET province = :province, district = :district, ward = :ward, detail = :detail 
            WHERE userId = :userId AND id = :id
            """)
    public void updateAddress(@BindBean Address address);

    @SqlUpdate("DELETE FROM address WHERE id = :id")
    public void deleteAddress(@Bind("addressId") int addressId);

    @SqlQuery("SELECT id, province, ward, district, detail FROM address WHERE id = :addressId")
    public List<Address> getAddressById(@Bind("addressId") String addressId);
}