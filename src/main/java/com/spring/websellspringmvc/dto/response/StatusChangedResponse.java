package com.spring.websellspringmvc.dto.response;

import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
