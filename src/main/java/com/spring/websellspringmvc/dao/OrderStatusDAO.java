package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.OrderStatus;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusDAO {

    @SqlQuery("SELECT id, typeStatus FROM order_statuses")
    public List<OrderStatus> getListAllOrderStatus();

    @SqlQuery("SELECT id, typeStatus FROM order_statuses WHERE id = :orderStatusId")
    public OrderStatus getOrderStatusById(@Bind("orderStatusId") int orderStatusId);
}
