package com.spring.websellspringmvc.services.http.shipping;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "giaoHanhNhanh", url = "${app.service.delivery.url}")
public interface GiaoHangNhanhHttp {
    @RequestMapping(method = RequestMethod.GET,
            value = "/v2/shipping-order/leadtime",
            consumes = "application/json")
    GiaoHangNhanhLeadDayResponse getLeadTime(
            @RequestHeader("token") String token,
            @RequestHeader("shop_id") String shopId,
            @RequestParam("from_province_id") String fromProvinceId,
            @RequestParam("from_district_id") String fromDistrictId,
            @RequestParam("from_ward_code") String fromWardCode,
            @RequestParam("to_province_id") String toProvinceId,
            @RequestParam("to_district_id") String toDistrictId,
            @RequestParam("to_ward_code") String toWardCode,
            @RequestParam("weight") Integer weight,
            @RequestParam("service_type_id") Integer serviceTypeId
    );

    @RequestMapping(method = RequestMethod.GET,
            value = "/v2/shipping-order/fee",
            consumes = "application/json")
    GiaoHangNhanhFeeResponse getFee(
            @RequestHeader("token") String token,
            @RequestHeader("shop_id") String shopId,
            @RequestParam("from_province_id") String fromProvinceId,
            @RequestParam("from_district_id") String fromDistrictId,
            @RequestParam("from_ward_code") String fromWardCode,
            @RequestParam("to_province_id") String toProvinceId,
            @RequestParam("to_district_id") String toDistrictId,
            @RequestParam("to_ward_code") String toWardCode,
            @RequestParam("weight") Integer weight,
            @RequestParam("service_type_id") Integer serviceTypeId
    );
}
