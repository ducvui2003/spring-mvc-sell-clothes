package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.OrderStatus;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusDAO {

    @SqlQuery("SELECT id, alias, typeStatus FROM order_statuses WHERE order_statuses.id <> 6")
    @RegisterBeanMapper(OrderStatus.class)
    public List<OrderStatus> getListAllOrderStatus();

    @SqlQuery("SELECT id, typeStatus FROM order_statuses WHERE id = :orderStatusId")
    @RegisterBeanMapper(OrderStatus.class)
    public OrderStatus getOrderStatusById(@Bind("orderStatusId") int orderStatusId);

    @SqlQuery("SELECT order_statuses.alias FROM order_statuses JOIN orders ON order_statuses.id = orders.orderStatusId WHERE orders.id = :orderId")
    public String getOrderStatusByOrderId(@Bind("orderId") String orderId);


    default com.spring.websellspringmvc.utils.constraint.OrderStatus getOrderStatus(String orderId) {
        String status = getOrderStatusByOrderId(orderId);
        return com.spring.websellspringmvc.utils.constraint.OrderStatus.valueOf(status.toUpperCase());
    }
}
