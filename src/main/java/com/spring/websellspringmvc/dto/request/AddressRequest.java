package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddressRequest {
    @JsonAlias("province")
    String provinceId;
    @JsonAlias("district")
    String districtId;
    @JsonAlias("ward")
    String wardId;
    String detail;
    String provinceName;
    String districtName;
    String wardName;

    public String exportAddressString() {
        return String.format("%s, %s, %s, %s", detail, wardName, districtName, wardName);
    }
}
