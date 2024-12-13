package com.spring.websellspringmvc.dto.mvc.request;

import com.spring.websellspringmvc.utils.constraint.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CheckoutFormData {
    List<Integer> cartItemId;
    String voucher;
    Integer addressId;
    String email;
    String fullName;
    String phone;
    PaymentMethod paymentMethod;
}
