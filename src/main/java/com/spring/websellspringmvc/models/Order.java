package com.spring.websellspringmvc.models;

import com.spring.websellspringmvc.utils.constraint.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jdbi.v3.core.enums.EnumByName;

import java.time.LocalDateTime;

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
    String signatureKey;
    int voucherId;
    String message;
    String province;
    String district;
    String ward;
    String detail;
    double fee;
    LocalDateTime leadTime;

    @EnumByName
    PaymentMethod paymentMethod;
    String paymentRef;

    LocalDateTime createAt;
//    String previousId;
}
