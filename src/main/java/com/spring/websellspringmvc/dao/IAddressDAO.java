package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Address;

import java.util.List;

public interface IAddressDAO {
    List<Address> getAddress(int userId);

    Integer insertAddress(Address address);

    void updateAddress(Address address);

    void deleteAddress(int addressId);
}
