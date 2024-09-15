package com.spring.websellspringmvc.config;

import com.spring.websellspringmvc.dao.AddressDAO;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;


@Configuration
public class JDBIConfig {
    @Bean
    public Jdbi jdbi(DataSource ds, List<JdbiPlugin> plugins, List<RowMapper<?>> mappers) {
        TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(ds);
        Jdbi jdbi = Jdbi.create(proxy);
        plugins.forEach(plugin -> jdbi.installPlugin(plugin));
        mappers.forEach(mapper -> jdbi.registerRowMapper(mapper));
        return jdbi;
    }

    @Bean
    public AddressDAO addressDAO(Jdbi jdbi) {
        return jdbi.onDemand(AddressDAO.class);
    }
}
