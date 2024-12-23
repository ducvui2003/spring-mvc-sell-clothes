package com.spring.websellspringmvc.dto.response;

import com.spring.websellspringmvc.models.Color;
import com.spring.websellspringmvc.models.Size;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusChangedResponse {
    List<OrderStatus> orderStatusTarget;
    List<TransactionStatus> transactionStatusTarget;
    List<OrderDetailItemChangedResponse> items;
}
