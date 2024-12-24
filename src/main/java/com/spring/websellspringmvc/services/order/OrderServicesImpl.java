package com.spring.websellspringmvc.services.order;

import ch.qos.logback.core.util.Loader;
import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dao.AddressDAO;
import com.spring.websellspringmvc.dao.KeyDAO;
import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dao.OrderStatusDAO;
import com.spring.websellspringmvc.dto.request.AddressRequest;
import com.spring.websellspringmvc.dto.request.ChangeOrderRequest;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderResponse;
import com.spring.websellspringmvc.models.Address;
import com.spring.websellspringmvc.models.Key;
import com.spring.websellspringmvc.services.address.AddressServices;
import com.spring.websellspringmvc.services.checkout.CheckoutServices;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.services.mail.MailVerifyOrderServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.SignedOrderFile;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServicesImpl implements OrderServices {
    OrderDAO orderDAO;
    CloudinaryUploadServices cloudinaryUploadServices;
    CheckoutServices checkoutServices;
    AddressDAO addressDAO;
    Jdbi jdbi;
    SignedOrderFile signedOrderFile;
    KeyDAO keyDAO;
    SessionManager sessionManager;

    @Override
    public List<OrderResponse> getOrder(int userId, int statusOrder) throws Exception {
        List<OrderResponse> listOrderResponse = orderDAO.getOrder(userId, statusOrder);
        List<OrderDetailResponse> orderDetailResponses = getOrderByOrderId(listOrderResponse.stream().map(OrderResponse::getId).toList());
        this.updateOrdersStatus(orderDetailResponses);
        return orderDAO.getOrder(userId, statusOrder);
    }

    @Override
    public List<OrderDetailItemResponse> getOrderDetailByOrderId(String orderId) {
        return orderDAO.getOrderDetailsByOrderId(orderId).stream().peek(
                orderItem -> orderItem.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), orderItem.getThumbnail()))).toList();
    }

    @Override
    public OrderDetailResponse getOrderByOrderId(String orderId, int userId) {
        Optional<OrderDetailResponse> orderDetailResponseDTO = orderDAO.getOrderByOrderDetailId(orderId);
        if (orderDetailResponseDTO.isPresent()) {
            OrderDetailResponse order = orderDetailResponseDTO.get();
            List<OrderDetailItemResponse> orderDetails = getOrderDetailByOrderId(orderId);
            if (orderDetails == null) return null;
            order.setItems(orderDetails);
            return order;
        }
        return null;
    }

    @Override
    public List<OrderDetailResponse> getOrderByOrderId(List<String> orderIds) {
        List<OrderDetailResponse> result = new ArrayList<>();
        for (String orderId : orderIds) {
            OrderDetailResponse order = getOrderByOrderId(orderId, sessionManager.getUser().getId());
            result.add(order);
        }
        return result;
    }

    @Override
    public void changeOrder(String orderId, Integer userId, ChangeOrderRequest request) {
        jdbi.useTransaction(handle -> {
            Address address = addressDAO.getAddressById(request.getAddressId());
            if (address == null) throw new AppException(ErrorCode.NOT_VALID);
            double fee = checkoutServices.getFeeShipping(address.getProvinceId(), address.getDistrictId(), address.getWardId());
            LocalDateTime leadTime = checkoutServices.getLeadTime(address.getProvinceId(), address.getDistrictId(), address.getWardId());

            OrderStatus orderStatus = OrderStatus.valueOf(orderDAO.getStatusById(orderId));

            // Nếu người dùng đã xác thực thì chuyển sang trạng thái y/c xác thực lại
            if (orderStatus == OrderStatus.PENDING)
                orderStatus = OrderStatus.CHANGED;

            int rowEffect = orderDAO.changeInfoOrder(orderId, userId, request, leadTime, fee, orderStatus.getValue());
            log.info("row effect: {}", rowEffect);
            if (rowEffect == 0) {
                throw new AppException(ErrorCode.UPDATE_FAILED);
            }
        });
    }

    @Override
    public void updateOrderStatusVerify(String orderId, int userId) {
        jdbi.useTransaction(handle -> {
            orderDAO.updateOrderStatus(orderId, OrderStatus.PENDING.getValue());
            log.info("Update order status success");
        });
    }

    @Override
    public void insertSignature(String orderId, String signature, String keyId) {
        jdbi.useTransaction(handle -> {
            if (orderDAO.insertSignature(orderId, signature, keyId) == 0)
                throw new AppException(ErrorCode.UPDATE_FAILED);
            log.info("Insert signature success");
        });
    }


    @Override
    public void updateOrdersStatus(List<OrderDetailResponse> orders) throws Exception {
        int userId = sessionManager.getUser().getId();

        jdbi.useTransaction(handle -> {
            for (OrderDetailResponse order : orders) {
                if (order.getStatus().equals(OrderStatus.VERIFYING.getDisplayName()) || order.getStatus().equals(OrderStatus.CHANGED.getDisplayName())) {
                    continue;
                }
                String signatureKey = order.getSignatureKey();
                if (signatureKey == null || signatureKey.isEmpty()) {
                    orderDAO.updateOrderStatus(order.getOrderId(), OrderStatus.CHANGED.getValue());
                }
                String hash = signedOrderFile.hashData(order);
                Key key = keyDAO.getKeyById(order.getKeyUsingVerify());
                PublicKey publicKey = KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key.getPublicKey())));

                boolean isSimilar = signedOrderFile.verifyData(hash.getBytes(), signatureKey, publicKey);
                if (!isSimilar) {
                    orderDAO.updateOrderStatus(order.getOrderId(), OrderStatus.CHANGED.getValue());
                    MailVerifyOrderServices mail = new MailVerifyOrderServices(order.getEmail(), order.getOrderId());
                    try {
                        mail.send();
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
