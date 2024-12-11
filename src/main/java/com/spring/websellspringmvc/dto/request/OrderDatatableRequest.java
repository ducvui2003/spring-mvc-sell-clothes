package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    String searchSelect;
    String contentSearch;
    String startDate;
    String endDate;
    @JsonProperty("deliveryMethod[]")
    String[] paymentMethod;
    @JsonProperty("orderStatus[]")
    String[] orderStatus;
    @JsonProperty("transactionStatus[]")
    String[] transactionStatus;
}
