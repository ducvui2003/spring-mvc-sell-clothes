package com.spring.websellspringmvc.services.order;

import ch.qos.logback.core.util.Loader;
import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dao.AddressDAO;
import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dao.OrderStatusDAO;
import com.spring.websellspringmvc.dto.request.AddressRequest;
import com.spring.websellspringmvc.dto.request.ChangeOrderRequest;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderResponse;
import com.spring.websellspringmvc.models.Address;
import com.spring.websellspringmvc.services.address.AddressServices;
import com.spring.websellspringmvc.services.checkout.CheckoutServices;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<OrderResponse> getOrder(int userId, int statusOrder) {
        return orderDAO.getOrder(userId, statusOrder);
    }

    @Override
    public List<OrderDetailItemResponse> getOrderDetailByOrderId(String orderId) {
        return orderDAO.getOrderDetailsByOrderId(orderId).stream().peek(
                orderItem -> orderItem.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), orderItem.getThumbnail()))).toList();
    }

    @Override
    public OrderDetailResponse getOrderByOrderId(String orderId, int userId) {
        Optional<OrderDetailResponse> orderDetailResponseDTO = orderDAO.getOrderByOrderDetailId(orderId, userId);
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
    public void changeOrder(String orderId, Integer userId, ChangeOrderRequest request) {
        jdbi.useTransaction(handle -> {
            if (orderDAO.backupOrder(orderId, userId) == 0) throw new AppException(ErrorCode.UPDATE_FAILED);
            Address address = addressDAO.getAddressById(request.getAddressId());
            if (address == null) throw new AppException(ErrorCode.NOT_VALID);
            double fee = checkoutServices.getFeeShipping(address.getProvinceId(), address.getDistrictId(), address.getWardId());
            LocalDateTime leadTime = checkoutServices.getLeadTime(address.getProvinceId(), address.getDistrictId(), address.getWardId());
            int rowEffect = orderDAO.changeInfoOrder(orderId, userId, request, leadTime, fee);
            log.info("row effect: {}", rowEffect);
            if (rowEffect == 0) {
                throw new AppException(ErrorCode.UPDATE_FAILED);
            }
        });
    }

    @Override
    public void updateOrderStatusVerify(String orderId, int userId) {
        jdbi.useTransaction(handle -> {
            if (orderDAO.backupOrder(orderId, userId) == 0) throw new AppException(ErrorCode.UPDATE_FAILED);
            orderDAO.updateOrderStatus(orderId, OrderStatus.PENDING.getValue());
            log.info("Update order status success");
        });
    }
}
