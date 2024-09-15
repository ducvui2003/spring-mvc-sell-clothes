package com.spring.websellspringmvc.dto.response.datatable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewDatatableResponse {
    int id;
    int userId;
    String productName;
    int orderDetailId;
    int ratingStar;
    String feedback;
    Date reviewDate;
    boolean visibility;
}
