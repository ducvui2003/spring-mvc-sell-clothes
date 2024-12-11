package com.spring.websellspringmvc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    Integer id;
    String wardName;
    String districtName;
    String provinceName;
    String wardId;
    String districtId;
    String provinceId;
    String detail;
}
