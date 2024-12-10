package com.spring.websellspringmvc.services.checkout;

import com.spring.websellspringmvc.dao.CartDAO;
import com.spring.websellspringmvc.dao.CheckoutDAO;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.AddressServices;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckoutServicesImpl implements CheckoutServices {
    CheckoutDAO checkoutDAO;
    AddressServices addressServices;
    CartDAO cartDAO;
    SessionManager sessionManager;
    CloudinaryUploadServices cloudinaryUploadServices;

    //    Trả về danh sách các sản phẩm được ngưởi dùng chọn mua
    @Override
    public List<CartItemResponse> getCarts(List<Integer> listCartItemId, Integer userId) {
        return cartDAO.getCart(listCartItemId, userId).stream().peek(cartResponse -> cartResponse.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), cartResponse.getThumbnail()))).toList();
    }

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
