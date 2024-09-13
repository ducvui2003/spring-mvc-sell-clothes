package com.spring.websellspringmvc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Review {
    int id;
    int orderDetailId;
    int ratingStar;
    String feedback;
    Date reviewDate;
    boolean isVisibility;

}
