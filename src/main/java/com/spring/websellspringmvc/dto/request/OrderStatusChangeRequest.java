package com.spring.websellspringmvc.dto.request;

import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderStatusChangeRequest {
    OrderStatus orderStatus;
    TransactionStatus transactionStatus;
}
