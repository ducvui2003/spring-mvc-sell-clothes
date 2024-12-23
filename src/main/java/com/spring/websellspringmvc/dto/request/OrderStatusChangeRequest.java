package com.spring.websellspringmvc.dto.request;

import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChangeRequest {
    OrderStatus orderStatus;
    TransactionStatus transactionStatus;
    List<OrderItemChangeRequest> items;

    @Data
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemChangeRequest {
        int id;
        int sizeId;
        int colorId;
        int quantity;
    }
}
