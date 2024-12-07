package com.spring.websellspringmvc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Address {
    Integer id;
    Integer userId;
    String detail;
    String province;
    String district;
    String ward;

    public String exportAddressString() {
        return String.format("%s, %s, %s, %s", detail, ward, district, province);
    }
}