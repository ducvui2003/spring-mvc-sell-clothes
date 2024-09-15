package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Product;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface DashboardDAO {

    @SqlQuery("SELECT count(*) FROM users")
    public int countUser();

    @SqlQuery("SELECT count(*) FROM products")
    public int countProduct();

    @SqlQuery("SELECT count(*) FROM orders")
    public int countOrder();

    @SqlQuery("SELECT count(*) FROM reviews")
    public int countReview();

    @SqlQuery("SELECT COUNT(*) count FROM orders WHERE MONTH(dateOrder) = :month AND YEAR(dateOrder) = :year AND orderStatusId = 4")
    public Long quantityOrderSuccess(@Bind("month") String month, @Bind("year") String year);

    @SqlQuery("SELECT COUNT(*) count FROM orders WHERE MONTH(dateOrder) = :month AND YEAR(dateOrder) = :year AND orderStatusId = 5")
    public Long quantityOrderFailed(@Bind("month") String month, @Bind("year") String year);

    @SqlQuery("""
            SELECT products.id, products.name, SUM(quantityRequired) as total 
            FROM orders JOIN (order_details JOIN products ON products.id = order_details.productId) ON orders.id = order_details.orderId 
            WHERE YEAR(orders.dateOrder) = :year AND MONTH(orders.dateOrder) = :month AND orders.orderStatusId = 4 
            GROUP BY products.id, products.name 
            ORDER BY total DESC 
            LIMIT 5
            """)
    public Map<Product, Integer> getListProductPopular(String month, String year);

    @SqlQuery("""
            SELECT products.id, products.name, SUM(quantityRequired) as total 
            FROM orders JOIN (order_details JOIN products ON products.id = order_details.productId) ON orders.id = order_details.orderId 
            WHERE YEAR(orders.dateOrder) = :year AND MONTH(orders.dateOrder) = :month AND orders.orderStatusId = 4 
            GROUP BY products.id, products.name 
            ORDER BY total ASC 
            LIMIT 5
            """)
    public Map<Product, Integer> getListProductNotPopular(@Bind("month") String month, @Bind("year") String year);

    @SqlQuery("""
            SELECT SUM(order_details.quantityRequired * order_details.price) FROM `order_details` join orders on order_details.orderId = orders.id 
            WHERE MONTH(orders.dateOrder) = :month AND YEAR(orders.dateOrder) = :year AND orders.orderStatusId = 4 
            """)
    public Double getRevenueByMonth(@Bind("month") String month, @Bind("year") String year);
}
