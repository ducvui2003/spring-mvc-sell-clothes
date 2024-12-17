package com.spring.websellspringmvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Address {
    Integer id;
    Integer userId;
    String wardId;
    String wardName;
    String districtId;
    String districtName;
    String provinceId;
    String provinceName;
    String detail;
}