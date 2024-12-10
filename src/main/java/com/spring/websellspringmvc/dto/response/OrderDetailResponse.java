package com.spring.websellspringmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderDetailResponse implements Serializable {
    private String orderId;
    private String status;
    private String fullName;
    private String phone;
    private String email;
    private String province;
    private String district;
    private String ward;
    private String detail;
    private String payment;
    private String voucherId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date orderDate;
    List<OrderDetailItemResponse> items;
}
