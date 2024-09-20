package com.spring.websellspringmvc.services.http.facebook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facebookClientUserInfo", url = "${app.service.facebook.user-info-uri}")
public interface FacebookClientUserInfo {
    @GetMapping
    FacebookUser getUserInfo(@RequestParam("access_token") String accessToken);
}
