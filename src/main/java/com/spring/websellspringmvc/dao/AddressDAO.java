package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AddressDAO implements IAddressDAO {
    GeneralDAO generalDAO;

    @Override
    public List<Address> getAddress(int userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, province, ward, district, detail FROM address WHERE userId = ?");
        return generalDAO.executeQueryWithSingleTable(sql.toString(), Address.class, userId);
    }

    public Integer insertAddress(Address address) {
        String sql = "INSERT INTO address (userId, province, district, ward, detail) VALUES (?, ?,?,?,?)";
        return generalDAO.executeInsert(sql, address.getUserId(), address.getProvince(), address.getDistrict(), address.getWard(), address.getDetail());
    }

    public void updateAddress(Address address) {
        String sql = "UPDATE address SET province = ?, district = ?, ward = ?, detail = ? WHERE userId = ? AND id = ";
        List<Address> list = generalDAO.executeQueryWithSingleTable(sql.toString(), Address.class, address.getUserId());
        generalDAO.executeAllTypeUpdate(sql, address.getProvince(), address.getDistrict(), address.getWard(), address.getDetail(), address.getUserId(), address.getId());
    }

    @Override
    public void deleteAddress(int addressId) {
        String sql = "DELETE FROM address WHERE id = ?";
        generalDAO.executeAllTypeUpdate(sql, addressId);
    }

    public List<Address> getAddressById(String addressId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, province, ward, district, detail FROM address WHERE id = ?");
        return generalDAO.executeQueryWithSingleTable(sql.toString(), Address.class, addressId);
    }
}