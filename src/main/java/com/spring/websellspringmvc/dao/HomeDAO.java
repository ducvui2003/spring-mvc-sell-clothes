package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Slider;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeDAO {

    //HardCode
    @SqlQuery(
            """
                    SELECT id, `name`, `description`, salePrice, originalPrice FROM products 
                    WHERE visibility = 1 AND createAt >= DATE_SUB('2023-12-01', INTERVAL 1 MONTH)
                    """
    )
    public List<Product> getListNewProducts();

    @SqlQuery("SELECT nameSlide, nameImage FROM sliders WHERE visibility = 1")
    public List<Slider> getListSlideShow();

    @SqlQuery("""
            SELECT products.id, products.`name`, products.`description`, products.salePrice, products.originalPrice FROM products 
            INNER JOIN order_details ON products.id = order_details.productId 
            WHERE products.visibility = 1
            GROUP BY products.id, products.`name`, products.salePrice, products.originalPrice
            HAVING SUM(order_details.quantityRequired) >= 10
            ORDER BY SUM(order_details.quantityRequired) DESC
            """)

    public List<Product> getListTrendProducts();
}
