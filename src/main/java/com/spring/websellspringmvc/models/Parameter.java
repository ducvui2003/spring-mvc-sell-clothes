package com.spring.websellspringmvc.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    int id;
    String name;
    double minValue;
    double maxValue;
    String unit;
    int categoryId;
    String guideImg;
}
