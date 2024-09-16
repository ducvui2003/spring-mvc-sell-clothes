package com.spring.websellspringmvc.models;

import com.spring.websellspringmvc.utils.MoneyRange;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFilter {
    Pageable pageable;
    long offset;
    long limit;
    List<Integer> categoryId;
    List<Integer> sizeId;
    List<Integer> colorId;
    List<MoneyRange> moneyRange;

    public static ProductFilter of(Pageable pageable, List<Integer> categoryId, List<Integer> sizeId, List<Integer> colorId, List<String> moneyRange) {
        return new ProductFilter(pageable, pageable.getOffset(), pageable.getPageSize(), categoryId, sizeId, colorId, getMoneyRange(moneyRange));
    }

    private static List<MoneyRange> getMoneyRange(List<String> moneyRange) {
        if (moneyRange == null) return new ArrayList<>();
        List<MoneyRange> moneyRangeList = new ArrayList<>();
        for (String s : moneyRange) {
            StringTokenizer token = new StringTokenizer(s, "-");
            try {
                double from = Double.parseDouble(token.nextToken());
                double to = Double.parseDouble(token.nextToken());
                moneyRangeList.add(new MoneyRange(from, to));
            } catch (NumberFormatException e) {
                log.error("Error: ", e);
                throw new RuntimeException(e);
            }
        }
        return moneyRangeList;
    }
}
