package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Slider;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.data.domain.Pageable;
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
    @RegisterBeanMapper(Product.class)
    public List<Product> getListNewProducts();

    @SqlQuery("SELECT nameSlide, nameImage FROM sliders WHERE visibility = 1")
    @RegisterBeanMapper(Slider.class)
    public List<Slider> getListSlideShow();

    @SqlQuery("""
            SELECT products.id, products.`name`, products.`description`, products.salePrice, products.originalPrice FROM products 
            INNER JOIN order_details ON products.id = order_details.productId 
            WHERE products.visibility = 1
            GROUP BY products.id, products.`name`, products.salePrice, products.originalPrice
            HAVING SUM(order_details.quantityRequired) >= 10
            ORDER BY SUM(order_details.quantityRequired) DESC
            """)
    @RegisterBeanMapper(Product.class)
    public List<Product> getListTrendProducts();

    @SqlQuery("""
            SELECT products.id, products.`name`, products.`description`, products.salePrice, products.originalPrice FROM products 
            INNER JOIN order_details ON products.id = order_details.productId 
            WHERE products.visibility = 1
            GROUP BY products.id, products.`name`, products.salePrice, products.originalPrice
            HAVING SUM(order_details.quantityRequired) >= 10
            ORDER BY SUM(order_details.quantityRequired) DESC
            LIMIT :limit OFFSET :offset
            """)
    @RegisterBeanMapper(Product.class)
    public List<Product> getListTrendProducts(@Bind("limit") int limit, @Bind("offset") long offset);


    @SqlQuery("""
            SELECT COUNT(*) 
            FROM products 
            INNER JOIN order_details ON products.id = order_details.productId 
            WHERE products.visibility = 1
            GROUP BY products.id, products.`name`, products.salePrice, products.originalPrice
            HAVING SUM(order_details.quantityRequired) >= 10
            ORDER BY SUM(order_details.quantityRequired) DESC
            """)
    public int countTrendProducts();
}
