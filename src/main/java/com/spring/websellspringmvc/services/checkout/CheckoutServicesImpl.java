package com.spring.websellspringmvc.services.checkout;

import com.spring.websellspringmvc.dao.AddressDAO;
import com.spring.websellspringmvc.dao.CartDAO;
import com.spring.websellspringmvc.dao.CheckoutDAO;
import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dto.mvc.request.CheckoutFormData;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.address.AddressServicesImpl;
import com.spring.websellspringmvc.services.http.shipping.GiaoHangNhanhFeeResponse;
import com.spring.websellspringmvc.services.http.shipping.GiaoHangNhanhHttp;
import com.spring.websellspringmvc.services.http.shipping.GiaoHangNhanhLeadDayResponse;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckoutServicesImpl implements CheckoutServices {
    CheckoutDAO checkoutDAO;
    AddressServicesImpl addressServicesImpl;
    CartDAO cartDAO;
    SessionManager sessionManager;
    CloudinaryUploadServices cloudinaryUploadServices;
    GiaoHangNhanhHttp giaoHangNhanhHttp;
    OrderDAO orderDAO;
    AddressDAO addressDAO;
    @Value("${app.service.delivery.token}")
    @NonFinal
    String token;
    @Value("${app.service.delivery.shop-id}")
    @NonFinal
    String shopId;
    @Value("${app.service.delivery.province-id}")
    @NonFinal
    String provinceIdShop;
    @Value("${app.service.delivery.district-id}")
    @NonFinal
    String districtIdShop;
    @Value("${app.service.delivery.ward-code}")
    @NonFinal
    String wardCodeShop;
    @Value("${app.service.delivery.weight}")
    @NonFinal
    Integer weight;
    @Value("${app.service.delivery.service-type-id}")
    @NonFinal
    Integer serviceTypeId;

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

    @Override
    public String createOrder(CheckoutFormData request, Integer userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setPaymentMethodId(request.getPaymentMethodId());
        order.setFullName(request.getFullName());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setOrderStatusId(1);
        order.setTransactionStatusId(1);

        Address address = addressDAO.getAddressById(request.getAddressId());
        order.setProvince(address.getProvinceName());
        order.setDistrict(address.getDistrictName());
        order.setWard(address.getWardName());
        order.setDetail(address.getDetail());

        double fee = getFeeShipping(address.getProvinceId(), address.getDistrictId(), address.getWardName());
        order.setFee(fee);

        String orderId = UUID.randomUUID().toString();
        order.setId(orderId);
        orderDAO.createOrder(order);
        createOrderDetail(request.getCartItemId(), orderId, userId);

        return orderId;
    }

    private void createOrderDetail(List<Integer> cartItems, String orderId, Integer userId) {
        List<OrderDetail> orderDetails = cartDAO.getOrderDetailPreparedAdded(cartItems, userId);
        orderDetails.forEach(orderDetail -> orderDetail.setOrderId(orderId));
        orderDAO.createOrderDetails(orderDetails);
    }

    private double getFeeShipping(String provinceId, String districtId, String wardCode) {
        GiaoHangNhanhFeeResponse response = giaoHangNhanhHttp.getFee(
                token,
                shopId,
                provinceIdShop,
                districtIdShop,
                wardCodeShop,
                provinceId,
                districtId,
                wardCode,
                weight,
                serviceTypeId
        );
        return response.getTotal();
    }

    private int getLeadTime(String provinceId, String districtId, String wardCode) {
        GiaoHangNhanhLeadDayResponse response = giaoHangNhanhHttp.getLeadTime(
                token,
                shopId,
                provinceIdShop,
                districtIdShop,
                wardCodeShop,
                provinceId,
                districtId,
                wardCode,
                weight,
                serviceTypeId
        );
        return response.getLeadTime();
    }
}
