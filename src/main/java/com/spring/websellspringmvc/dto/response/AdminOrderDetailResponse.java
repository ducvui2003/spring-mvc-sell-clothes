package com.spring.websellspringmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminOrderDetailResponse implements Serializable {
    String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDateTime dateOrder;
    String fullName;
    String email;
    String phone;
    String paymentMethod;
    String transactionStatus;
    String orderStatus;
    int voucherId;
    String province;
    String district;
    String ward;
    String detail;
    double fee;
    LocalDateTime createAt;
    String previousId;
    List<OrderDetailItemResponse> items;
}
