package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.OrderDetail;
import com.spring.websellspringmvc.models.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RegisterBeanMapper(OrderDetail.class)
public interface OrderDetailDAO {
    @SqlQuery("SELECT orderId,productId, productName, sizeRequired, colorRequired, quantityRequired, price FROM order_details WHERE id = :id")
    public List<OrderDetail> getOrderDetailById(@Bind("id") int id);


    @SqlUpdate("DELETE FROM order_details WHERE orderId IN (<orderIds>)")
    public void removeOrderDetailByMultipleOrderId(@BindList("orderIds") String[] orderIds);


    @SqlQuery("""
            SELECT users.id, users.username, users.fullName, users.avatar 
            FROM users JOIN orders ON users.id = orders.userId 
            WHERE orders.id = (SELECT orderId FROM order_details WHERE id = :orderDetailId)
            """)
    @RegisterBeanMapper(User.class)
    public Optional<User> getUserByIdProductDetail(@Bind("orderDetailId") int orderDetailId);

    @SqlQuery(""" 
            SELECT id, orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price 
            FROM order_details WHERE orderId = :orderId 
            """)
    public List<OrderDetail> getListOrderDetailByOrderId(@Bind("orderId") String orderId);
}
