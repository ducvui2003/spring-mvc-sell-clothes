package com.spring.websellspringmvc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatatableResponse<T> {
    long draw;
    long recordsTotal;
    long recordsFiltered;
    List<T> data;
}
