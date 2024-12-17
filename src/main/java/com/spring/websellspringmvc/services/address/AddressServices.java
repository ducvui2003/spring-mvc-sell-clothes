package com.spring.websellspringmvc.services.address;

import com.spring.websellspringmvc.dto.request.AddressRequest;
import com.spring.websellspringmvc.dto.response.AddressResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface AddressServices {
    boolean validate(AddressRequest request) throws URISyntaxException, IOException;

    void createAddress(AddressRequest request, Integer userId) throws URISyntaxException, IOException;

    void updateAddress(AddressRequest request, Integer userId, Integer addressId) throws URISyntaxException, IOException;

    List<AddressResponse> getAddress(int userId);

    void deleteAddress(Integer addressId, Integer userId);
}
