package com.spring.websellspringmvc.services.http.shipping;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiaoHangNhanhLeadDayResponse {
    @JsonAlias("leadtime")
    Integer leadTime;
}
