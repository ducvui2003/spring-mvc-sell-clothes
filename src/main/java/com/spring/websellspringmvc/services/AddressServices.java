package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.AddressDAO;
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
public class AddressServices {
    @Value("${app.service.map4d.url}")
    @NonFinal
    String url;
    @Value("${app.service.map4d.api-key}")
    @NonFinal
    String apiKey;
    AddressDAO addressDAO;

    boolean validateAddress(String address) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url)
                .addParameter("address", address)
                .addParameter("key", apiKey).build();
        HttpResponse response = Request.Get(uri).execute().returnResponse();
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode == 200;
    }

    public Integer insertAddress(Address address) throws URISyntaxException, IOException {
        if (!validateAddress(address.exportAddressString())) {
            return -1;
        }
        return addressDAO.insertAddress(address);
    }

    public boolean updateAddress(Address address) throws URISyntaxException, IOException {
        if (!validateAddress(address.exportAddressString())) {
            return false;
        }
        addressDAO.updateAddress(address);
        return true;
    }

    public List<Address> getAddress(int userId) {
        List<Address> addressList = addressDAO.getAddress(userId);
        if (addressList.isEmpty())
            return null;
        return addressList;
    }

    public boolean deleteAddress(int addressId, int userId) {
        List<Address> addressList = addressDAO.getAddress(userId);
        if (addressList.isEmpty())
            return false;
        if (addressList.stream().noneMatch(address -> address.getId() == addressId))
            return false;
        addressDAO.deleteAddress(addressId);
        return true;
    }

    public Address getAddressById(String address) {
        List<Address> addressList = addressDAO.getAddressById(address);
        if (addressList.isEmpty())
            return null;
        return addressList.get(0);
    }
}