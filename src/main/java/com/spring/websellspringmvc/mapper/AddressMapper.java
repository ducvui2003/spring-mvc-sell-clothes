package com.spring.websellspringmvc.mapper;

import com.spring.websellspringmvc.dto.request.AddressRequest;
import com.spring.websellspringmvc.dto.response.AddressResponse;
import com.spring.websellspringmvc.models.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponse toAddressResponse(Address address);

    Address toAddress(AddressRequest request);

}
