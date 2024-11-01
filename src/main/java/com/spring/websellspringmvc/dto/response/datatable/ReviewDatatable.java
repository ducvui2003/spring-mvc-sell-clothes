package com.spring.websellspringmvc.dto.response.datatable;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewDatatable {
    int id;
    int userId;
    String productName;
    int orderDetailId;
    int ratingStar;
    String feedback;
    Date reviewDate;
    boolean visibility;
}
