package com.spring.websellspringmvc.mapper;

import com.spring.websellspringmvc.dto.response.ReviewDetailResponse;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    ReviewDetailResponse.OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    ReviewDetailResponse.OrderResponse toOrderResponse(Order order);


}
