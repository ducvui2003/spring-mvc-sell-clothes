package com.spring.websellspringmvc.dto.request;

import com.spring.websellspringmvc.models.Voucher;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherDetailRequest {
  Voucher voucher;
  String state;
  List<Integer> listIdProduct;
}
