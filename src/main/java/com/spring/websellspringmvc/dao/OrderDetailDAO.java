package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.OrderDetail;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDAO {
    @SqlQuery("SELECT orderId,productId, productName, sizeRequired, colorRequired, quantityRequired, price FROM order_details WHERE id = :id")
    public List<OrderDetail> getOrderDetailById(@Bind("id") int id);

    @SqlQuery("SELECT id, orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price FROM order_details WHERE orderId = :orderId")
    public List<OrderDetail> getListOrderDetailByOrderId(@Bind("orderId") String orderId);

    @SqlUpdate("DELETE FROM order_details WHERE orderId IN (<orderIds>)")
    public void removeOrderDetailByMultipleOrderId(@BindList("orderIds") String[] orderIds);
}
