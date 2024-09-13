package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.OrderStatus;

import java.util.List;

public class OrderStatusDao {

    public List<OrderStatus> getListAllOrderStatus() {
        String sql = "SELECT id, typeStatus FROM order_statuses";
        return GeneralDAO.executeQueryWithSingleTable(sql, OrderStatus.class);
    }

    public OrderStatus getOrderStatusById(int orderStatusId) {
        String sql = "SELECT id, typeStatus FROM order_statuses WHERE id = ?";
        return GeneralDAO.executeQueryWithSingleTable(sql, OrderStatus.class, orderStatusId).get(0);
    }
}
