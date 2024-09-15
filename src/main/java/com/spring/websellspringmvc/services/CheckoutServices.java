package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.CheckoutDAO;
import com.spring.websellspringmvc.models.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckoutServices {
    CheckoutDAO checkoutDAO;
    AddressServices addressServices;


    public List<DeliveryMethod> getAllInformationDeliveryMethod() {
        return checkoutDAO.getAllInformationDeliveryMethod();
    }

    public List<PaymentMethod> getAllPaymentMethod() {
        return checkoutDAO.getAllPaymentMethod();
    }

    public DeliveryMethod getDeliveryMethodById(int id) {
        return checkoutDAO.getDeliveryMethodById(id);
    }

    public PaymentMethod getPaymentMethodById(int id) {
        return checkoutDAO.getPaymentMethodById(id);
    }

    public PaymentOwner getPaymentOwnerByPaymentMethodId(int id) {
        return checkoutDAO.getPaymentOwnerByPaymentMethodId(id);
    }

    public void addNewOrder(int orderId, int userId, String dateOrder, String fullName, String email, String phone, String address, Integer deliveryMethodId, int paymentMethodId, Integer voucherId) {
        Address adr = addressServices.getAddressById(address);
        checkoutDAO.addOrder(orderId, userId, dateOrder, fullName, email, phone, adr, deliveryMethodId, paymentMethodId, voucherId);
    }

    public void addEachOrderDetail(int orderId, int productId, String productName, String sizeRequired, String colorRequired, int quantityRequired, double price) {
        checkoutDAO.addOrderDetail(orderId, productId, productName, sizeRequired, colorRequired, quantityRequired, price);
    }
}
