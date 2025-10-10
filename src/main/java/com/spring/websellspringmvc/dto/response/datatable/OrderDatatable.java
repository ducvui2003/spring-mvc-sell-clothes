package com.spring.websellspringmvc.dto.response.datatable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDatatable {
  String id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
  LocalDateTime dateOrder;

  String fullName;
  String paymentMethod;
  OrderStatus orderStatus;
  TransactionStatus transactionStatus;
}
