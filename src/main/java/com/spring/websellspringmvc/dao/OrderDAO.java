package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderResponse;
import com.spring.websellspringmvc.models.*;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDAO {
    @SqlQuery("SELECT id, typePayment FROM payment_methods")
    @RegisterBeanMapper(PaymentMethod.class)
    public List<PaymentMethod> getListAllPaymentMethodManage();


    @SqlQuery("SELECT * FROM orders WHERE id = :id")
    public Order getOrderById(@Bind("id") String id);


    @SqlUpdate("UPDATE orders SET orderStatusId = :orderStatusId, transactionStatusId = :transactionStatusId WHERE id = :orderId")
    public void updateStatusByOrderId(@Bind("orderId") String orderId, @Bind("orderStatusId") int orderStatusId, @Bind("transactionStatusId") int transactionStatusId);

    @SqlUpdate("UPDATE orders SET orderStatusId = :orderStatusId WHERE id = :orderId")
    public void updateOrderStatus(@Bind("orderId") String orderId, @Bind("orderStatusId") int orderStatusId);

    @SqlUpdate("UPDATE orders SET transactionStatusId = :transactionStatusId WHERE id = :orderId")
    public void updateOrderTransaction(@Bind("orderId") String orderId, @Bind("transactionStatusId") int transactionStatusId);

    @SqlQuery("SELECT id, code, description, minimumPrice, discountPercent FROM vouchers WHERE id = :id")
    public Voucher getVoucherById(@Bind("id") int id);

    @SqlQuery("SELECT COUNT(*) count FROM orders")
    public long getQuantity();

    @SqlQuery("select * from orders limit :limit offset :offset")
    public List<Order> getLimit(@Bind("limit") int limit, @Bind("offset") int offset);

    //    User
    @SqlQuery("""
            SELECT orders.id AS id, orders.dateOrder AS dateOrder, COUNT(order_details.orderId) AS quantity 
            FROM orders JOIN order_details ON orders.id = order_details.orderId 
            WHERE orders.orderStatusId = :statusOrder AND orders.userId = :userId AND orders.previousId IS NULL
            GROUP BY order_details.orderId 
            """)
    @RegisterBeanMapper(OrderResponse.class)
    public List<OrderResponse> getOrder(@Bind("userId") int userId, @Bind("statusOrder") int statusOrder);

    @SqlQuery("""
            SELECT id,  productId, quantityRequired, price 
            FROM order_details 
            WHERE orderId IN (<listId>)
            """)
    public List<OrderDetail> getOrderDetailByOrderId(@BindList("listId") List<String> listId);

    @SqlQuery("""
            SELECT DISTINCT id, name, categoryId 
            FROM products 
            WHERE id = :id
            """)
    public List<Product> getProductInOrderDetail(@Bind("id") int id);

    @SqlQuery("""
            SELECT nameImage 
            FROM images 
            WHERE productId = :id
            """)
    public List<Image> getNameImageByProductId(@Bind("id") int id);

    @SqlQuery("""
            SELECT order_details.id 
            FROM orders JOIN order_details ON orders.id = order_details.orderId 
            WHERE orders.userId = :userId AND orders.orderStatusId = 4 
            AND order_details.id NOT IN (SELECT reviews.orderDetailId FROM reviews) 
            """)
    public List<OrderDetail> getOrderDetailNotReview(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT order_details.id 
            FROM orders JOIN order_details ON orders.id = order_details.orderId 
            WHERE orders.userId = :userId AND orders.orderStatusId = 4 
            AND order_details.id IN (SELECT reviews.orderDetailId FROM reviews) 
            """)
    public List<OrderDetail> getOrderDetailHasReview(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT orders.id, 
            order_statuses.typeStatus, 
            orders.fullName, 
            orders.phone, 
            orders.email, 
            orders.province, 
            orders.district, 
            orders.ward, orders.detail, orders.voucherId, orders.dateOrder 
            FROM orders JOIN order_statuses ON orders.orderStatusId=order_statuses.id 
            WHERE orders.id = :orderId
            """)
    @RegisterBeanMapper(OrderDetailResponse.class)
    public Optional<OrderDetailResponse> getOrderByOrderDetailId(@Bind("orderId") String orderId);

    @SqlQuery("""
            SELECT 
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
            INSERT INTO orders (id, userId, paymentMethod, paymentRef, fullName, email, phone, orderStatusId, transactionStatusId, voucherId,
                                fee, province, district, ward, detail)
            SELECT :order.id,
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
            SELECT orders.id                       as orderId,
                   dateOrder,
                   fullName,
                   email,
                   phone,
                   paymentMethod,
                   order_statuses.typeStatus       AS orderStatus,
                   transaction_statuses.typeStatus AS transactionStatus,
                   voucherId,
                   province,
                   district,
                   ward,
                   detail,
                   fee
            FROM orders
                     JOIN order_statuses ON orders.orderStatusId = order_statuses.id
                     JOIN transaction_statuses ON orders.transactionStatusId = transaction_statuses.id
            WHERE orders.id = :id AND orders.previousId IS NULL
            """)
    @RegisterBeanMapper(AdminOrderDetailResponse.class)
    public AdminOrderDetailResponse getOrder(@Bind("id") String id);

    @SqlQuery("""
            SELECT orders.id                       as orderId,
                   dateOrder,
                   fullName,
                   email,
                   phone,
                   paymentMethod,
                   order_statuses.typeStatus       AS orderStatus,
                   transaction_statuses.typeStatus AS transactionStatus,
                   voucherId,
                   province,
                   district,
                   ward,
                   detail,
                   fee 
            FROM orders 
                     JOIN order_statuses ON orders.orderStatusId = order_statuses.id 
                     JOIN transaction_statuses ON orders.transactionStatusId = transaction_statuses.id 
            WHERE orders.previous = :id 
            ORDER BY orders.createAt DESC
            """)
    @RegisterBeanMapper(Order.class)
    List<AdminOrderDetailResponse> getOrderPrevious(@Bind("id") String id);

    @SqlUpdate("""
            UPDATE orders
            SET orders.transactionStatusId = :transactionStatus
            WHERE orders.paymentRef = :paymentRef
            """)
    void updateTransactionStatusVNPay(@Bind("paymentRef") String paymentRef, @Bind("transactionStatus") int value);
}