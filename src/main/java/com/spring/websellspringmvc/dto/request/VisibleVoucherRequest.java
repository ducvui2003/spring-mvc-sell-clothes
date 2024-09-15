package com.spring.websellspringmvc.dto.request;

import com.spring.websellspringmvc.services.voucher.VoucherState;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VisibleVoucherRequest {
    @NotNull
    String code;
    @NotNull
    VoucherState state;
}
