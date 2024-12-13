package com.spring.websellspringmvc.config;

import feign.Client;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LoggingFeignClientInterceptor extends Client.Default {

    public LoggingFeignClientInterceptor() {
        super(null, null);
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        // Log the request details
        log.info("Feign Request: [{} {}], Headers: {}", request.httpMethod(), request.url(), request.headers());

        // Execute the request
        Response response = super.execute(request, options);

        // Log the response details
        log.info("Feign Response: Status: {}, Headers: {}", response.status(), response.headers());

        if (response.body() != null) {
            // Read and log the response body
            String responseBody = new String(response.body().asInputStream().readAllBytes());
            log.info("Feign Response Body: {}", responseBody);
        }

        return response;
    }
}
