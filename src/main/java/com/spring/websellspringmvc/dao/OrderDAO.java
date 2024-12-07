package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.OrderDetailResponseDTO;
import com.spring.websellspringmvc.dto.OrderItemResponseDTO;
import com.spring.websellspringmvc.dto.OrderResponseDTO;
import com.spring.websellspringmvc.models.*;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrderDAO {
    @SqlQuery("""
            SELECT id, userId, dateOrder, deliveryMethodId, paymentMethodId,
                             fullName, email, phone, address, orderStatusId, transactionStatusId, 
                             voucherId FROM orders WHERE 1=1
                             And if(:searchSelect = 'orderId', id LIKE :contentSearch, fullName LIKE :contentSearch)
                             And if(:paymentMethod != null, paymentMethodId IN (:paymentMethod), 1=1)
                             And if(:orderStatus != null, orderStatusId IN (:orderStatus), 1=1)
                             And if(:transactionStatus != null, transactionStatusId IN (:transactionStatus), 1=1)\
                             And if(:startDate != null AND :endDate != null, dateOrder BETWEEN startDate AND endDate, 1=1)
            """)
    List<Order> getListOrdersBySearchFilter(
            @BindList("paymentMethod") String[] paymentMethod,
            @BindList("orderStatus") String[] orderStatus,
            @BindList("transactionStatus") String[] transactionStatus,
            @Bind("contentSearch") String contentSearch,
            @Bind("searchSelect") String searchSelect,
            @Bind("startDate") String startDate,
            @Bind("endDate") String endDate);


    @SqlQuery("SELECT id, userId, dateOrder, deliveryMethodId, paymentMethodId, fullName, email, phone, address, orderStatusId, transactionStatusId, voucherId FROM orders")
    public List<Order> getListAllOrders();

    @SqlQuery("SELECT id, typePayment FROM payment_methods")
    public List<PaymentMethod> getListAllPaymentMethodManage();

    @SqlQuery("SELECT id, typeShipping, description, shippingFee FROM delivery_methods")
    public List<DeliveryMethod> getListAllDeliveryMethodManage();

    @SqlQuery("SELECT id, typePayment FROM payment_methods WHERE id = :id")
    public PaymentMethod getPaymentMethodMangeById(@Bind("id") int id);

    @SqlQuery("SELECT id, typeShipping, description, shippingFee FROM delivery_methods WHERE id = :id")
    public DeliveryMethod getDeliveryMethodManageById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM orders WHERE id = :id")
    public Order getOrderById(@Bind("id") String id);

    @SqlUpdate("DELETE FROM orders WHERE id IN (ids)")
    public void removeOrderByMultipleId(@BindList("ids") String[] multipleId);

    @SqlUpdate("UPDATE orders SET orderStatusId = 5 WHERE id IN (<ids>)")
    public void cancelOrderByArrayMultipleId(@BindList("ids") String[] multipleId);

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
            WHERE orders.orderStatusId = :statusOrder AND orders.userId = :userId 
            GROUP BY order_details.orderId 
            """)
    @RegisterBeanMapper(OrderResponseDTO.class)
    public List<OrderResponseDTO> getOrder(@Bind("userId") int userId, @Bind("statusOrder") int statusOrder);

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
            SELECT orders.id, order_statuses.typeStatus, orders.fullName, orders.phone, orders.email, orders.province, orders.district, orders.ward, orders.detail, orders.voucherId, orders.dateOrder AS orderDate 
            FROM orders JOIN order_statuses ON orders.orderStatusId=order_statuses.id 
            WHERE orders.id = :orderId
            """)
    public Optional<OrderDetailResponseDTO> getOrderByOrderDetailId(@Bind("orderId") String orderId);

    @SqlQuery("""
            SELECT order_details.productName AS name, order_details.quantityRequired AS quantity, order_details.sizeRequired AS size, order_details.colorRequired AS color, order_details.price AS price, images.nameImage AS thumbnail \\n" +
            FROM (
                SELECT productId, MIN(images.id) AS minImageId 
                FROM images
                GROUP BY productId
                ) AS minImages
            JOIN images ON images.productId = minImages.productId AND images.id = minImages.minImageId
            JOIN order_details ON order_details.productId = images.productId
            WHERE order_details.orderId = ?"
            """)
    public List<OrderItemResponseDTO> getOrderDetailsByOrderId(String orderId);

}