package com.spring.websellspringmvc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.spring.websellspringmvc.models.Voucher;

import java.util.List;

@Data
@NoArgsConstructor
public class VoucherDetailRequest {
    Voucher voucher;
    String state;
    List<Integer> listIdProduct;
}
