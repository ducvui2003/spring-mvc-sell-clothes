package com.spring.websellspringmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDetailResponse implements Serializable {
    String orderId;
    String status;
    String fullName;
    String phone;
    String email;
    String province;
    String district;
    String signatureKey;
    String keyUsingVerify;
    String ward;
    String detail;
    String payment;
    String voucherId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    LocalDateTime dateOrder;
    List<OrderDetailItemResponse> items;
    double fee;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    LocalDateTime leadTime;
}
