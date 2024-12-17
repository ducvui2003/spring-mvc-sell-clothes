package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderRequest {
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
}
