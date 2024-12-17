package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderResponse;
import com.spring.websellspringmvc.models.Image;
import com.spring.websellspringmvc.models.OrderDetail;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoryService {
    OrderDAO orderDAO;
    CloudinaryUploadServices cloudinaryUploadServices;

    public List<OrderResponse> getOrder(int userId, int statusOrder) {
        return orderDAO.getOrder(userId, statusOrder);
    }


    public List<OrderDetailItemResponse> getOrderDetailByOrderId(String orderId) {
        return orderDAO.getOrderDetailsByOrderId(orderId).stream().peek(
                orderItem -> orderItem.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), orderItem.getThumbnail()))).toList();
    }

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
}
