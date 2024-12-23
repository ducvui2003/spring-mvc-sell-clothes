package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.request.ChangeOrderRequest;
import com.spring.websellspringmvc.dto.request.OrderStatusChangeRequest;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderResponse;
import com.spring.websellspringmvc.models.*;
import org.jdbi.v3.sqlobject.config.KeyColumn;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.ValueColumn;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrderDAO {

    @SqlQuery("SELECT * FROM orders WHERE id = :id")
    @RegisterBeanMapper(Order.class)
    public Order getOrderById(@Bind("id") String id);

    @SqlUpdate("UPDATE orders SET orderStatusId = :orderStatusId, transactionStatusId = :transactionStatusId WHERE id = :orderId")
    public void updateStatusByOrderId(@Bind("orderId") String orderId, @Bind("orderStatusId") int orderStatusId, @Bind("transactionStatusId") int transactionStatusId);


    @SqlUpdate("UPDATE orders SET orderStatusId = :orderStatusId WHERE id = :orderId")
    public int updateOrderStatusByOrderId(@Bind("orderId") String orderId, @Bind("orderStatusId") int orderStatusId);

    @SqlUpdate("UPDATE orders SET transactionStatusId = :transactionStatusId WHERE id = :orderId")
    public int updateTransactionStatusByOrderId(@Bind("orderId") String orderId, @Bind("transactionStatusId") int transactionStatusId);

    @SqlUpdate("""
               UPDATE cart_items
               SET 
                   color_id = CASE
                           WHEN color_id != :colorId THEN :colorId
                           ELSE color_id
                   END,
                   size_id = CASE
                           WHEN size_id != :sizeId THEN :sizeId 
                           ELSE size_id
                   END,
                   quantity = CASE
                           WHEN quantity != :quantity THEN :quantity 
                           ELSE quantity
                   END
               WHERE id = :id
            """)
    public int updateOrderDetail(@BindBean OrderStatusChangeRequest.OrderItemChangeRequest orderDetails);

    @SqlUpdate("UPDATE orders SET orderStatusId = :orderStatusId WHERE id = :orderId")
    void updateOrderStatus(@Bind("orderId") String orderId, @Bind("orderStatusId") int orderStatusId);

    @SqlUpdate("UPDATE orders SET transactionStatusId = :transactionStatusId WHERE id = :orderId")
    public void updateOrderTransaction(@Bind("orderId") String orderId, @Bind("transactionStatusId") int transactionStatusId);

    @SqlQuery("SELECT id, code, description, minimumPrice, discountPercent FROM vouchers WHERE id = :id")
    public Voucher getVoucherById(@Bind("id") int id);

    @SqlQuery("""
            SELECT 
                    'orderStatus' AS key, order_statuses.alias AS value
                FROM orders 
                JOIN order_statuses ON orders.orderStatusId = order_statuses.id 
                WHERE orders.id = :id
                UNION ALL
                SELECT 
                    'transactionStatus' AS key, transaction_statuses.alias AS value
                FROM orders 
                JOIN transaction_statuses ON orders.transactionStatusId = transaction_statuses.id 
                WHERE orders.id = :id
            """)
    @KeyColumn("key")
    @ValueColumn("value")
    public Map<String, String> getStatusById(@Bind("id") String id);

    @SqlQuery("""
            SELECT orders.id AS id, orders.dateOrder AS dateOrder 
            FROM orders 
            WHERE orders.orderStatusId = :orderStatus 
            AND orders.userId = :userId 
            AND orders.previousId = orders.id
            """)
    @RegisterBeanMapper(OrderResponse.class)
    public List<OrderResponse> getOrder(@Bind("userId") int userId, @Bind("orderStatus") int orderStatus);

    @SqlQuery("""
            SELECT orders.id as orderId, 
            order_statuses.typeStatus as status, 
            orders.fullName, 
            orders.phone, 
            orders.email, 
            orders.province, 
            orders.district, 
            orders.ward, 
            orders.detail, 
            orders.voucherId, 
            orders.dateOrder, 
            orders.paymentMethod as payment, 
            orders.fee as fee,
            orders.leadTime as leadTime 
            FROM orders JOIN order_statuses ON orders.orderStatusId=order_statuses.id 
            WHERE orders.id = :orderId
            """)
    @RegisterBeanMapper(OrderDetailResponse.class)
    public Optional<OrderDetailResponse> getOrderByOrderDetailId(@Bind("orderId") String orderId, @Bind("userId") int userId);

    @SqlQuery("""
            SELECT 
            order_details.id AS id,
            order_details.productId AS productId,
            order_details.productName AS name, 
            order_details.quantityRequired AS quantity, 
            order_details.sizeRequired AS size, 
            order_details.colorRequired AS color, 
            order_details.price AS price, 
            images.nameImage AS thumbnail 
            FROM (
                SELECT productId, MIN(images.id) AS minImageId 
                FROM images
                GROUP BY productId
                ) AS minImages
            JOIN images ON images.productId = minImages.productId AND images.id = minImages.minImageId
            JOIN order_details ON order_details.productId = images.productId
            WHERE order_details.orderId = ?
            """)
    @RegisterBeanMapper(OrderDetailItemResponse.class)
    public List<OrderDetailItemResponse> getOrderDetailsByOrderId(String orderId);


    @SqlUpdate("""
            INSERT INTO orders (id, previousId, userId, paymentMethod, paymentRef, fullName, email, phone, orderStatusId, transactionStatusId, voucherId,
                                fee, leadTime, province, district, ward, detail)
            SELECT :order.id,
                    :order.previousId,
                   :order.userId,
                   :order.paymentMethod,
                   :order.paymentRef,
                   :order.fullName,
                   :order.email,
                   :order.phone,
                   :order.orderStatusId,
                   :order.transactionStatusId,
                   :order.voucherId,
                   :order.fee,
                   :order.leadTime,
                   a.provinceName,
                   a.districtName,
                   a.wardName,
                   a.detail
            FROM address a
            WHERE a.id = :addressId AND a.userId = :order.userId
            """)
    public void createOrder(@BindBean("order") Order order, @Bind("addressId") int addressId);

    @SqlBatch("""
            INSERT INTO order_details (orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price)
            VALUES (:orderId, :orderDetail.productId, :orderDetail.productName, :orderDetail.sizeRequired, :orderDetail.colorRequired, :orderDetail.quantityRequired, :orderDetail.price)
            """)
    public void createOrderDetails(
            @Bind("orderId") String orderId,
            @BindBean("orderDetail") OrderDetail... orderDetails);

    @SqlQuery("""
            SELECT orders.id                       as id,
                   dateOrder,
                   fullName,
                   email,
                   phone,
                   paymentMethod,
                   order_statuses.alias       AS orderStatus,
                   transaction_statuses.alias AS transactionStatus,
                   voucherId,
                   province,
                   district,
                   ward,
                   detail,
                   fee,
                   leadTime 
            FROM orders
                     JOIN order_statuses ON orders.orderStatusId = order_statuses.id
                     JOIN transaction_statuses ON orders.transactionStatusId = transaction_statuses.id
            WHERE orders.id = :id AND orders.previousId = orders.id
            """)
    @RegisterBeanMapper(AdminOrderDetailResponse.class)
    public AdminOrderDetailResponse getOrder(@Bind("id") String id);

    @SqlQuery("""
            SELECT orders.id                       as id,
                   dateOrder,
                   fullName,
                   email,
                   phone,
                   paymentMethod,
                   order_statuses.alias       AS orderStatus,
                   transaction_statuses.alias AS transactionStatus,
                   voucherId,
                   province,
                   district,
                   ward,
                   detail,
                   fee,
                   leadTime,
                   createAt,
                   previousId
            FROM orders 
                     JOIN order_statuses ON orders.orderStatusId = order_statuses.id 
                     JOIN transaction_statuses ON orders.transactionStatusId = transaction_statuses.id 
            WHERE orders.previousId = :id OR orders.id = :id
            ORDER BY orders.createAt DESC
            """)
    @RegisterBeanMapper(AdminOrderDetailResponse.class)
    List<AdminOrderDetailResponse> getOrderPrevious(@Bind("id") String id);

    @SqlUpdate("""
            UPDATE orders
            SET orders.transactionStatusId = :transactionStatus
            WHERE orders.paymentRef = :paymentRef
            """)
    void updateTransactionStatusVNPay(@Bind("paymentRef") String paymentRef, @Bind("transactionStatus") int value);

    @SqlUpdate("""
            UPDATE orders o
            JOIN address a ON a.id = :request.addressId
            SET o.province = a.provinceName,
                o.district = a.districtName,
                o.ward = a.wardName,
                o.detail = a.detail,
                o.fee = :fee,
                o.leadTime = :leadTime,
                o.orderStatusId = 7
            WHERE o.id = :orderId AND o.userId = :userId
            """)
    int changeInfoOrder(
            @Bind("orderId") String orderId,
            @Bind("userId") int userId,
            @BindBean("request") ChangeOrderRequest request,
            @Bind("leadTime") LocalDateTime leadTime,
            @Bind("fee") double fee
    );

    @SqlUpdate("""
            INSERT INTO orders (
            id, 
            userId, 
            dateOrder, 
            paymentMethod, 
            paymentRef, 
            fullName, 
            email, 
            phone, 
            transactionStatusId, 
            orderStatusId, 
            province, 
            district, 
            ward, 
            detail, 
            fee, 
            leadTime,
            previousId
            )
            SELECT   UUID(),
                     o.userId,
                     o.dateOrder,
                     o.paymentMethod,
                     o.paymentRef,
                     o.fullName,
                     o.email,
                     o.phone,
                     o.transactionStatusId,
                     o.orderStatusId,
                     o.province,
                     o.district,
                     o.ward,
                     o.detail,
                     o.fee,
                     o.leadTime,
                     :orderId 
            FROM orders o
            WHERE o.id = :orderId AND o.previousId = :orderId AND o.userId = :userId
            """)
    int backupOrder(@Bind("orderId") String orderId, @Bind("userId") int userId);

    @SqlBatch("""
            IF EXISTS (SELECT * FROM order
             WHERE orderId = :orderId AND 
             fullName=:orderDetail.fullName AND 
             email=:orderDetail.email AND 
             phone=:orderDetail.phone AND 
             province=:orderDetail.province AND 
             district=:orderDetail.district AND 
             ward=:orderDetail.ward AND 
             detail=:orderDetail.detail AND 
             paymentMethod=:orderDetail.paymentMethod AND 
             voucherId=:orderDetail.voucherId AND 
             fee=:orderDetail.fee)
            """)
    boolean[] verifyHistory(@BindBean("orderDetail") List<AdminOrderDetailResponse> orderDetail);

    @SqlQuery("""
            SELECT colors.*
            FROM order_details JOIN products ON order_details.productId = products.id
            JOIN colors ON products.id = colors.productId
            WHERE order_details.orderId = :orderId
            """)
    @RegisterBeanMapper(Color.class)
    List<Color> getColorsByOrderId(@Bind("orderId") String orderId);

    @SqlQuery("""
            SELECT sizes.*
            FROM order_details JOIN products ON order_details.productId = products.id
            JOIN sizes ON products.id = sizes.productId
            WHERE order_details.orderId = :orderId
            """)
    @RegisterBeanMapper(Size.class)
    List<Size> getSizesByOrderId(@Bind("orderId") String orderId);
}