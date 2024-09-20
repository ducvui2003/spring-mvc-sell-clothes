package com.spring.websellspringmvc.config;

import com.spring.websellspringmvc.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class JDBIConfig {
    @Bean
    public Jdbi jdbi(DataSource dataSource) {
        log.info("Creating datasource...");
        return Jdbi.create(dataSource).installPlugin(new SqlObjectPlugin()).setSqlLogger(new CustomSQLLogger());
    }

    @Bean
    public AddressDAO addressDAO(Jdbi jdbi) {
        return jdbi.onDemand(AddressDAO.class);
    }

    @Bean
    public UserDAO userDAO(Jdbi jdbi) {
        return jdbi.onDemand(UserDAO.class);
    }

    @Bean
    public OrderDAO orderDAO(Jdbi jdbi) {
        return jdbi.onDemand(OrderDAO.class);
    }

    @Bean
    public VoucherDAO voucherDAO(Jdbi jdbi) {
        return jdbi.onDemand(VoucherDAO.class);
    }

    @Bean
    public CategoryDAO categoryDAO(Jdbi jdbi) {
        return jdbi.onDemand(CategoryDAO.class);
    }

    @Bean
    public ProductDAO productDAO(Jdbi jdbi) {
        return jdbi.onDemand(ProductDAO.class);
    }

    @Bean
    public ColorDAO colorDAO(Jdbi jdbi) {
        return jdbi.onDemand(ColorDAO.class);
    }

    @Bean
    public ContactDAO contactDAO(Jdbi jdbi) {
        return jdbi.onDemand(ContactDAO.class);
    }

    @Bean
    public ImageDAO imageDAO(Jdbi jdbi) {
        return jdbi.onDemand(ImageDAO.class);
    }

    @Bean
    public DashboardDAO dashboardDAO(Jdbi jdbi) {
        return jdbi.onDemand(DashboardDAO.class);
    }

    @Bean
    public OrderDetailDAO orderDetailDAO(Jdbi jdbi) {
        return jdbi.onDemand(OrderDetailDAO.class);
    }

    @Bean
    public OrderStatusDAO orderStatusDAO(Jdbi jdbi) {
        return jdbi.onDemand(OrderStatusDAO.class);
    }

    @Bean
    public ParameterDAO parameterDAO(Jdbi jdbi) {
        return jdbi.onDemand(ParameterDAO.class);
    }

    @Bean
    public ProductCardDAO productCardDAO(Jdbi jdbi) {
        return jdbi.onDemand(ProductCardDAO.class);
    }

    @Bean
    public ReviewDAO reviewDAO(Jdbi jdbi) {
        return jdbi.onDemand(ReviewDAO.class);
    }

    @Bean
    public SizeDAO sizeDAO(Jdbi jdbi) {
        return jdbi.onDemand(SizeDAO.class);
    }

    @Bean
    public TransactionStatusDAO transactionStatusDAO(Jdbi jdbi) {
        return jdbi.onDemand(TransactionStatusDAO.class);
    }

    @Bean
    public HomeDAO homeDAO(Jdbi jdbi) {
        return jdbi.onDemand(HomeDAO.class);
    }

    @Bean
    public CheckoutDAO checkoutDAO(Jdbi jdbi) {
        return jdbi.onDemand(CheckoutDAO.class);
    }
}
