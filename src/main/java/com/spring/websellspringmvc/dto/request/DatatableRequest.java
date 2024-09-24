package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
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
    Search search;
    Order[] order;
    Column[] columns;

    @Data
    public static class Column {
        String data;
        String name;
        boolean searchable;
        boolean orderable;
        Search search;
    }

    @Data
    public static class Search {
        String value;
        boolean regex;
    }

    @Data
    public static class Order {
        int column;
        String dir;
        String name;
    }
}