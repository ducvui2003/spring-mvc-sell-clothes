package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Address;
import com.spring.websellspringmvc.models.DeliveryMethod;
import com.spring.websellspringmvc.models.PaymentMethod;
import com.spring.websellspringmvc.models.PaymentOwner;

import java.util.List;

public class CheckoutDao {

    public static DeliveryMethod getDeliveryMethodById(int id) {
        String sql = "SELECT id, typeShipping, description, shippingFee FROM delivery_methods WHERE id = ?";
        return GeneralDAO.executeQueryWithSingleTable(sql, DeliveryMethod.class, id).get(0);
    }

    public List<DeliveryMethod> getAllInformationDeliveryMethod() {
        String sql = "SELECT id, typeShipping, description, shippingFee FROM delivery_methods";
        return GeneralDAO.executeQueryWithSingleTable(sql, DeliveryMethod.class);
    }

    public List<PaymentMethod> getAllPaymentMethod() {
        String sql = "SELECT id, typePayment FROM payment_methods";
        return GeneralDAO.executeQueryWithSingleTable(sql, PaymentMethod.class);
    }

    public PaymentMethod getPaymentMethodById(int id) {
        String sql = "SELECT id, typePayment FROM payment_methods WHERE id = ?";
        return GeneralDAO.executeQueryWithSingleTable(sql, PaymentMethod.class, id).get(0);
    }

    public PaymentOwner getPaymentOwnerByPaymentMethodId(int id) {
        String sql = "SELECT id, paymentMethodId, ownerName, paymentPlatform, accountNumber FROM payment_owner WHERE paymentMethodId = ?";
        return GeneralDAO.executeQueryWithSingleTable(sql, PaymentOwner.class, id).get(0);
    }

    public void addNewOrder(int orderId, int userId, String dateOrder, String fullName, String email, String phone, Address address, Integer deliveryMethodId, int paymentMethodId, Integer voucherId) {
        StringBuilder sql = new StringBuilder("INSERT INTO orders (id, userId, dateOrder, fullName, email, phone, address, deliveryMethodId, paymentMethodId, orderStatusId, transactionStatusId, voucherId, province, district, ward, detail)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        GeneralDAO.executeAllTypeUpdate(sql.toString(), orderId, userId, dateOrder, fullName, email, phone, address.exportAddressString(), deliveryMethodId, paymentMethodId, 1, 1, voucherId, address.getProvince(), address.getDistrict(), address.getWard(), address.getDetail());
    }

    public void addEachOrderDetail(int orderId, int productId, String productName, String sizeRequired, String colorRequired, int quantityRequired, double price) {
        StringBuilder sql = new StringBuilder("INSERT INTO order_details(orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");
        GeneralDAO.executeAllTypeUpdate(sql.toString(), orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price);
    }
}