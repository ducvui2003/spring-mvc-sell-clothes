package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.*;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutDAO {
    @SqlQuery("SELECT id, typeShipping, description, shippingFee FROM delivery_methods WHERE id = :id")
    @RegisterBeanMapper(DeliveryMethod.class)
    public DeliveryMethod getDeliveryMethodById(@Bind("id") int id);

    @SqlQuery("SELECT id, typeShipping, description, shippingFee FROM delivery_methods")
    @RegisterBeanMapper(DeliveryMethod.class)
    public List<DeliveryMethod> getAllInformationDeliveryMethod();

    @SqlQuery("SELECT id, typePayment FROM payment_methods")
    @RegisterBeanMapper(PaymentMethod.class)
    public List<PaymentMethod> getAllPaymentMethod();

    @SqlQuery("SELECT id, typePayment FROM payment_methods WHERE id = :id")
    @RegisterBeanMapper(PaymentMethod.class)
    public PaymentMethod getPaymentMethodById(@Bind("id") int id);

    @SqlQuery("SELECT id, paymentMethodId, ownerName, paymentPlatform, accountNumber FROM payment_owner WHERE paymentMethodId = :id")
    @RegisterBeanMapper(PaymentMethod.class)
    public PaymentOwner getPaymentOwnerByPaymentMethodId(@Bind("id") int id);

    @SqlUpdate("""
            INSERT INTO orders (id, userId, dateOrder, fullName, email, phone, address, deliveryMethodId, paymentMethodId, orderStatusId, transactionStatusId, voucherId, province, district, ward, detail)
            VALUES (:orderId, :userId, :dateOrder, :fullName, :email, :phone, :address, :deliveryMethodId, :paymentMethodId, 1, 1, :voucherId, :province, :district, :ward, :detail)
            """)
    public void addOrder(int orderId, int userId, String dateOrder, String fullName, String email, String phone, Address address, Integer deliveryMethodId, int paymentMethodId, Integer voucherId);
//    {
//        StringBuilder sql = new StringBuilder("INSERT INTO orders (id, userId, dateOrder, fullName, email, phone, address, deliveryMethodId, paymentMethodId, orderStatusId, transactionStatusId, voucherId, province, district, ward, detail)");
//        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//        GeneralDAO.executeAllTypeUpdate(sql.toString(), orderId, userId, dateOrder, fullName, email, phone, address.exportAddressString(), deliveryMethodId, paymentMethodId, 1, 1, voucherId, address.getProvince(), address.getDistrict(), address.getWard(), address.getDetail());
//    }


    @SqlUpdate("""
            INSERT INTO order_details(orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price)
            VALUES (:orderId, :productId, :productName, :sizeRequired, :colorRequired, :quantityRequired, :price)
            """)
    public void addOrderDetail(int orderId, int productId, String productName, String sizeRequired, String colorRequired, int quantityRequired, double price);
//    {
//        StringBuilder sql = new StringBuilder("INSERT INTO order_details(orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price)");
//        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");
//        GeneralDAO.executeAllTypeUpdate(sql.toString(), orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price);
//    }
}