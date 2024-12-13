package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.OrderStatus;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusDAO {

    @SqlQuery("SELECT id, typeStatus FROM order_statuses")
    @RegisterBeanMapper(OrderStatus.class)
    public List<OrderStatus> getListAllOrderStatus();

    @SqlQuery("SELECT id, typeStatus FROM order_statuses WHERE id = :orderStatusId")
    @RegisterBeanMapper(OrderStatus.class)
    public OrderStatus getOrderStatusById(@Bind("orderStatusId") int orderStatusId);
}
