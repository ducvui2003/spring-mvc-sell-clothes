package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.websellspringmvc.utils.constraint.SearchSelect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    @JsonProperty("deliveryMethod[]")
    int[] paymentMethod;
    @JsonProperty("orderStatus[]")
    int[] orderStatus;
    @JsonProperty("transactionStatus[]")
    int[] transactionStatus;
}
