package com.spring.websellspringmvc.services.http.facebook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facebookClientAccessToken", url = "${app.service.facebook.token-uri}")
public interface FacebookClientAccessToken {
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    FacebookAccessTokenResponse getAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code
    );
}
