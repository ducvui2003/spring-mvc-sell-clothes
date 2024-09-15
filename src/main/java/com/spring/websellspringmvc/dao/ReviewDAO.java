package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Review;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RegisterBeanMapper(Review.class)
public interface ReviewDAO {

    @SqlQuery("""
            SELECT orders.id 
            FROM orders JOIN order_details ON orders.id = order_details.orderId 
            WHERE orders.userId = :userId AND order_details.id = :orderProductIdRequest AND order_details.id IN (SELECT orderDetailId FROM reviews)
            """)
    public List<Review> checkReview(@Bind("userId") int userId, @Bind("orderProductIdRequest") int orderProductIdRequest);

    @SqlQuery(
            """
                    SELECT name 
                    FROM order_details JOIN products ON order_details.productId = products.id 
                    WHERE order_details.id = :orderProductId
                    """
    )
    public List<Product> getNameProduct(@Bind("orderProductId") int orderProductId);

    @SqlQuery(
            """
                    SELECT ratingStar 
                    FROM products JOIN (order_details JOIN reviews ON order_details.id = reviews.orderDetailId) ON products.id = order_details.productId 
                    WHERE products.id = :productId AND reviews.visibility = true
                    """
    )
    public List<Review> getReviewStar(@Bind("productId") int productId);

    @SqlQuery(
            """
                    SELECT reviews.orderDetailId, reviews.ratingStar, reviews.feedback, reviews.reviewDate 
                    FROM reviews JOIN order_details ON reviews.orderDetailId = order_details.id 
                    WHERE order_details.productId = :productId AND reviews.visibility = :visibility
                    """
    )
    public List<Review> getReviewByProductId(int productId, boolean visibility);

    @SqlQuery(
            """
                    SELECT order_details.productId AS orderDetailId 
                    FROM order_details JOIN reviews ON reviews.orderDetailId = order_details.id 
                    WHERE order_details.id = :orderDetailId
                    """
    )
    public List<Review> getReviewByOrderDetailId(@Bind("orderDetailId") int orderDetailId);

    @SqlUpdate("""
                        
            INSERT INTO reviews (orderDetailId, ratingStar, feedback, reviewDate) 
            VALUES (:orderDetailId, :ratingStar, :feedback, :reviewDate)
            """)
    public void createReview(@BindBean Review review);


    public List<Review> getReviews(int pageNumber, int limit);
//    {
//        int offset = (pageNumber - 1) * limit;
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT id, orderDetailId, ratingStar, feedback, reviewDate, visibility ")
//                .append("FROM reviews ")
//                .append(" LIMIT ")
//                .append(limit)
//                .append(" OFFSET ")
//                .append(offset);
//        return GeneralDAO.executeQueryWithSingleTable(sql.toString(), Review.class);
//    }

    @SqlQuery("SELECT COUNT(*) FROM reviews")
    public int getQuantityProduct();

    @SqlQuery("""
            SELECT products.name 
            FROM products JOIN order_details ON products.id = order_details.productId 
            WHERE order_details.id = :orderDetailId
            """)
    public List<Product> getNameProductByOrderDetailId(@Bind("orderDetailId") int orderDetailId);

    @SqlQuery("""
            SELECT orderDetailId 
            FROM reviews 
            WHERE id = :reviewId
            """)
    public int getOrderDetailId(@Bind("reviewId") int reviewId);

    @SqlQuery("""
            SELECT orderDetailId, ratingStar, feedback, reviewDate, visibility 
            FROM reviews 
            WHERE id = :reviewId
            """)
    public Review getReviewById(@Bind("reviewId") int reviewId);

    @SqlUpdate("UPDATE reviews SET visibility = :state WHERE id = :id")
    public void updateVisibility(@Bind("id") int id, @Bind("state") boolean state);

    @SqlQuery("SELECT * FROM reviews")
    public List<Review> getAll();
}
