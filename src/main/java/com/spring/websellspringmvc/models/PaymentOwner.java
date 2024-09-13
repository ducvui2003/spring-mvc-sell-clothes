package com.spring.websellspringmvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOwner {
    int id;
    int paymentMethodId;
    String ownerName;
    String paymentPlatform;
    String accountNumber;

}
