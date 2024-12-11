package com.spring.websellspringmvc.dto.response.datatable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDatatable {
    int id;
    Date dateOrder;
    String fullName;
    Integer paymentMethodId;
    Integer orderStatusId;
}
