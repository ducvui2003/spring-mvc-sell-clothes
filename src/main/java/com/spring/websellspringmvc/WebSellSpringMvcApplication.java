package com.spring.websellspringmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import java.util.Arrays;

@SpringBootApplication
@EnableJdbcHttpSession
@EnableFeignClients
public class WebSellSpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSellSpringMvcApplication.class, args);
    }

}
