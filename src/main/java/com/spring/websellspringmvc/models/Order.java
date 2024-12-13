package com.spring.websellspringmvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Order {
    String id;
    int userId;
    LocalDateTime dateOrder;
    int paymentMethodId;
    String fullName;
    String email;
    String phone;
    int orderStatusId;
    int transactionStatusId;
    int voucherId;
    String message;
    String province;
    String district;
    String ward;
    String detail;
    double fee;
}
