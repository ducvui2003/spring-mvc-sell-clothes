package com.spring.websellspringmvc.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherProduct {
    private Integer voucherId;
    private Integer productId;
}
