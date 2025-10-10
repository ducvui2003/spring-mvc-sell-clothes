package com.spring.websellspringmvc.dto.mvc.request;

import com.spring.websellspringmvc.utils.constraint.PaymentMethod;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CheckoutRequest {
  List<Integer> cartItemId;
  String voucher;
  Integer addressId;
  String email;
  String fullName;
  String phone;
  PaymentMethod paymentMethod;
}
