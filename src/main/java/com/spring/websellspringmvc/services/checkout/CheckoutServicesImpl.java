package com.spring.websellspringmvc.services.checkout;

import com.spring.websellspringmvc.dao.AddressDAO;
import com.spring.websellspringmvc.dao.CartDAO;
import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.mvc.request.CheckoutRequest;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.models.Address;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.OrderDetail;
import com.spring.websellspringmvc.services.http.shipping.GiaoHangNhanhFeeResponse;
import com.spring.websellspringmvc.services.http.shipping.GiaoHangNhanhHttp;
import com.spring.websellspringmvc.services.http.shipping.GiaoHangNhanhLeadDayResponse;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.services.vnpay.VnPayServices;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckoutServicesImpl implements CheckoutServices {
    CartDAO cartDAO;
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
    VnPayServices vnPayServices;
    Jdbi jdbi;

    OrderStatus statusBegin = OrderStatus.VERIFYING;

    //    Trả về danh sách các sản phẩm được ngưởi dùng chọn mua
    @Override
    public List<CartItemResponse> getCarts(List<Integer> listCartItemId, Integer userId) {
        return cartDAO.getCart(listCartItemId, userId).stream().peek(cartResponse -> cartResponse.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), cartResponse.getThumbnail()))).toList();
    }


    @Override
    public String createOrder(CheckoutRequest request, Integer userId) {
        String orderId = UUID.randomUUID().toString();
        jdbi.useTransaction(handle -> {
            Order order = new Order();
            order.setUserId(userId);
            order.setPaymentMethod(request.getPaymentMethod());
            order.setFullName(request.getFullName());
            order.setEmail(request.getEmail());
            order.setPhone(request.getPhone());
            order.setOrderStatusId(statusBegin.getValue());
            order.setTransactionStatusId(TransactionStatus.UN_PAID.getValue());

            Address address = addressDAO.getAddressById(request.getAddressId());
            order.setProvince(address.getProvinceName());
            order.setDistrict(address.getDistrictName());
            order.setWard(address.getWardName());
            order.setDetail(address.getDetail());

            double fee = getFeeShipping(address.getProvinceId(), address.getDistrictId(), address.getWardId());
            order.setFee(fee);

            order.setLeadTime(getLeadTime(address.getProvinceId(), address.getDistrictId(), address.getWardId()));

            order.setId(orderId);
            order.setPreviousId(orderId);
            orderDAO.createOrder(order, address.getId());
            createOrderDetail(request.getCartItemId(), orderId, userId);

            cartDAO.deleteCartItemIn(request.getCartItemId());

        });

        return orderId;
    }

    @Override
    public String createOrderByVnPay(CheckoutRequest request, Integer userId, String ip) throws UnsupportedEncodingException {

        Order order = new Order();
        order.setUserId(userId);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setFullName(request.getFullName());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setOrderStatusId(statusBegin.getValue());
        order.setTransactionStatusId(TransactionStatus.UN_PAID.getValue());

        Address address = addressDAO.getAddressById(request.getAddressId());
        order.setProvince(address.getProvinceName());
        order.setDistrict(address.getDistrictName());
        order.setWard(address.getWardName());
        order.setDetail(address.getDetail());

        double fee = getFeeShipping(address.getProvinceId(), address.getDistrictId(), address.getWardId());
        order.setFee(fee);
        order.setLeadTime(getLeadTime(address.getProvinceId(), address.getDistrictId(), address.getWardId()));

// Tạo order id và payment ref
        String orderId = UUID.randomUUID().toString();
        order.setId(orderId);
        order.setPaymentRef(orderId);
        orderDAO.createOrder(order, address.getId());

        double totalPrice = createOrderDetail(request.getCartItemId(), orderId, userId);

        cartDAO.deleteCartItemIn(request.getCartItemId());
        String urlPayment = vnPayServices.generateUrl(totalPrice, orderId, ip);
        return urlPayment;
    }


    // Tạo các order detail, thực hiện tính toán tổng số tiền cần thanh toán
    private double createOrderDetail(List<Integer> cartItems, String orderId, Integer userId) {
        List<OrderDetail> orderDetails = cartDAO.getOrderDetailPreparedAdded(cartItems, userId);
        orderDetails.forEach(orderDetail -> orderDetail.setOrderId(orderId));
        orderDAO.createOrderDetails(orderId, orderDetails.toArray(new OrderDetail[0]));
        return orderDetails.stream().map(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantityRequired()).reduce(Double::sum).orElse(0.0);
    }

    @Override
    public double getFeeShipping(String provinceId, String districtId, String wardCode) {
        ApiResponse<GiaoHangNhanhFeeResponse> response = giaoHangNhanhHttp.getFee(token, shopId, provinceIdShop, districtIdShop, wardCodeShop, provinceId, districtId, wardCode, weight, serviceTypeId);
        return response.getData().getTotal();
    }

    @Override
    public LocalDateTime getLeadTime(String provinceId, String districtId, String wardCode) {
        ApiResponse<GiaoHangNhanhLeadDayResponse> response = giaoHangNhanhHttp.getLeadTime(token, shopId, provinceIdShop, districtIdShop, wardCodeShop, provinceId, districtId, wardCode, weight, serviceTypeId);
        Optional<LocalDateTime> optionalLocalDateTime = Optional.of(LocalDateTime.ofEpochSecond(response.getData().getLeadTime(), 0, ZoneOffset.UTC));
        return optionalLocalDateTime.orElse(null);
    }

    @Override
    public void updateTransactionStatusVNPay(String paymentRef, TransactionStatus status) {
        orderDAO.updateTransactionStatusVNPay(paymentRef, status.getValue());
    }

    @Override
    public boolean verifyOrder(OrderDetailResponse orderDetail, List<AdminOrderDetailResponse> orderPrevious) {
        if (orderDetail == null || orderPrevious == null) {
            return false;
        }
//        boolean exsist = orderDAO.getOrderById(orderDetail.getOrderId());
        boolean[] exsistHistory = orderDAO.verifyHistory(orderPrevious);
        System.out.println("exsistHistory: " + exsistHistory);
        return false;
    }
}
