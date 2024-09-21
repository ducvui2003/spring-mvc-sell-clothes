package com.spring.websellspringmvc.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewFilter {
    Pageable pageable;
    long offset;
    long limit;
    int productId;

    public static ReviewFilter of(Pageable pageable, int productId) {
        return new ReviewFilter(pageable, pageable.getOffset(), pageable.getPageSize(), productId);
    }

}
