package com.spring.websellspringmvc.services.http.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleClientUserInfo", url = "${app.service.google.user-info-uri}")
public interface GoogleClientUserInfo {
    @GetMapping("")
    GoogleUser getUserInfo(@RequestParam("access_token") String accessToken);
}
