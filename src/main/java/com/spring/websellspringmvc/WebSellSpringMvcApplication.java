package com.spring.websellspringmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@EnableJdbcHttpSession
@EnableFeignClients
public class WebSellSpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSellSpringMvcApplication.class, args);
    }

}
