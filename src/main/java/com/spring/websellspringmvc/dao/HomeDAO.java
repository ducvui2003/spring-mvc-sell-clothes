package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Slider;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeDAO {
    @SqlQuery("""
            SELECT COUNT(*) 
            FROM products 
            WHERE visibility = 1 AND createAt >= DATE_SUB('2023-12-01', INTERVAL 1 MONTH)
            """)
    public long countNewProducts();

    @SqlQuery("SELECT nameSlide, nameImage FROM sliders WHERE visibility = 1")
    @RegisterBeanMapper(Slider.class)
    public List<Slider> getListSlideShow();

    @SqlQuery("""
            SELECT COUNT(*) 
            FROM products 
            INNER JOIN order_details ON products.id = order_details.productId 
            WHERE products.visibility = 1
            GROUP BY products.id, products.`name`, products.salePrice, products.originalPrice
            HAVING SUM(order_details.quantityRequired) >= 10
            ORDER BY SUM(order_details.quantityRequired) DESC
            """)
    public long countTrendProducts();
}
