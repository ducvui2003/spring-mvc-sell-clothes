package com.spring.websellspringmvc.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import com.spring.websellspringmvc.properties.DatabaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Getter
public class JDBIConfig {
    DatabaseProperties databaseProperties;

    @Bean
    public Jdbi makeConnect() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(databaseProperties.getUrl());
        dataSource.setUser(databaseProperties.getUsername());
        dataSource.setPassword(databaseProperties.getPassword());

        try {
            dataSource.setDefaultFetchSize(1000);
            dataSource.setUseCompression(true);
            dataSource.setAutoReconnect(true);
        } catch (SQLException throwables) {
            log.error("Error when set properties for datasource");
        }
        return Jdbi.create(dataSource);
    }
}
