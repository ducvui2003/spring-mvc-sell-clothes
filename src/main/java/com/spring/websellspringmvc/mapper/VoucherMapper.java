package com.spring.websellspringmvc.mapper;

import com.spring.websellspringmvc.dto.request.CreateVoucherRequest;
import com.spring.websellspringmvc.dto.request.UpdateVoucherRequest;
import com.spring.websellspringmvc.models.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);

    Voucher toVoucher(CreateVoucherRequest createVoucherRequest);

    Voucher toVoucher(UpdateVoucherRequest updateVoucherRequest);
}
