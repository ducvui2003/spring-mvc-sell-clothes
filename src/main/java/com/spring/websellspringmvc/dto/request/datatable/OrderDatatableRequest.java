package com.spring.websellspringmvc.dto.request.datatable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.websellspringmvc.utils.constraint.SearchSelect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDatatableRequest extends DatatableRequest {
    SearchSelect searchSelect;
    String contentSearch;
    String startDate;
    String endDate;
    @JsonProperty("deliveryMethod")
    List<Integer> paymentMethod;
    @JsonProperty("orderStatus")
    List<Integer> orderStatus;
    @JsonProperty("transactionStatus")
    List<Integer> transactionStatus;
}
