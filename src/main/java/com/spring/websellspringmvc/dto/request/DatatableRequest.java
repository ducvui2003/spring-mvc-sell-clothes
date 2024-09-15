package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DatatableRequest {
    long draw;
    int start;
    int length;
    @JsonProperty("search[value]")
    String searchValue;
    @JsonProperty("order[0][column]")
    int orderColumn = 0;
    @JsonProperty("order[0][dir]")
    String orderDir = "asc";
}
