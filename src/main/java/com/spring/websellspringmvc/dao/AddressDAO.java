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

    @SqlQuery("""
            SELECT id as id,
            wardName,
            districtName,
            provinceName,
            provinceId,
            districtId,
            wardId,
            detail
            FROM address WHERE userId = :userId
            """)
    public List<Address> getAddress(@Bind("userId") int userId);

    @SqlUpdate("""
            INSERT INTO address (userId, provinceId, districtId, wardId, detail, provinceName, districtName, wardName) 
            VALUES (:address.userId, :address.provinceId, :address.districtId, :address.wardId, :address.detail, 
                    :address.provinceName, :address.districtName, :address.wardName)
            """)
    @GetGeneratedKeys
    public int insertAddress(@BindBean("address") Address address);

    @SqlUpdate("""
            UPDATE address 
            SET provinceId = :address.provinceId, 
                districtId = :address.districtId, 
                wardId = :address.wardId, 
                detail = :address.detail,
                provinceName = :address.provinceName, 
                districtName = :address.districtName, 
                wardName = :address.wardName
            WHERE userId = :address.userId AND id = :address.id
            """)
    public int updateAddress(@BindBean("address") Address address);

    @SqlUpdate("DELETE FROM address WHERE id = :addressId AND userId = :userId")
    public int deleteAddress(@Bind("addressId") int addressId, @Bind("userId") int userId);

    @SqlQuery("""
            SELECT id, provinceId, wardId, districtId, detail, 
            provinceName, districtName, wardName
            FROM address WHERE id = :addressId
            """)
    public List<Address> getAddressById(@Bind("addressId") String addressId);
}