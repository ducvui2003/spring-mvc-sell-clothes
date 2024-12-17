package com.spring.websellspringmvc.services.address;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dao.AddressDAO;
import com.spring.websellspringmvc.dto.request.AddressRequest;
import com.spring.websellspringmvc.dto.response.AddressResponse;
import com.spring.websellspringmvc.mapper.AddressMapper;
import com.spring.websellspringmvc.models.Address;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressServicesImpl implements AddressServices {
    @Value("${app.service.map4d.url}")
    @NonFinal
    String url;
    @Value("${app.service.map4d.api-key}")
    @NonFinal
    String apiKey;
    AddressDAO addressDAO;
    AddressMapper addressMapper;

    @Override
    public List<AddressResponse> getAddress(int userId) {
        List<Address> addressList = addressDAO.getAddressByUserId(userId);
        if (addressList.isEmpty())
            return null;
        return addressList.stream().map(addressMapper::toAddressResponse).toList();
    }

    @Override
    public boolean validate(AddressRequest address) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url)
                .addParameter("address", address.exportAddressString())
                .addParameter("key", apiKey).build();
        HttpResponse response = Request.Get(uri).execute().returnResponse();
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode == 200;
    }

    @Override
    public void createAddress(AddressRequest request, Integer userId) {
        Address address = addressMapper.toAddress(request);
        address.setUserId(userId);
        int isSuccess = addressDAO.insertAddress(address);
        if (isSuccess == 0) {
            throw new AppException(ErrorCode.CREATE_FAILED);
        }
    }

    @Override
    public void updateAddress(AddressRequest request, Integer userId, Integer addressId) {
        Address address = addressMapper.toAddress(request);
        address.setUserId(userId);
        address.setId(addressId);
        int isSuccess = addressDAO.updateAddress(address);
        if (isSuccess == 0) {
            throw new AppException(ErrorCode.UPDATE_FAILED);
        }
    }

    @Override
    public void deleteAddress(Integer addressId, Integer userId) {
        int deleted = addressDAO.deleteAddress(addressId, userId);
        if (deleted == 0) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }
}