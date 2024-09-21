package com.spring.websellspringmvc.models;

import com.spring.websellspringmvc.utils.MoneyRange;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilter {
    Pageable pageable;
    long offset;
    long limit;
    List<Integer> categoryId;
    List<String> sizeNames;
    List<String> codeColors;
    List<MoneyRange> moneyRange;

    public static ProductFilter of(Pageable pageable, List<Integer> categoryId, List<String> sizeNames, List<String> codeColors, List<String> moneyRange) {
        return new ProductFilter(pageable, pageable.getOffset(), pageable.getPageSize(), categoryId, sizeNames, codeColors, convertMoneyRange(moneyRange));
    }

    private static List<MoneyRange> convertMoneyRange(List<String> moneyRange) {
        if (moneyRange == null) return null;
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

    public String getCategoryId() {
        if (categoryId == null) {
            return null;  // Return null if no categories
        }
        return categoryId.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public String getSizeNames() {
        if (sizeNames == null) {
            return null;  // Return null if no sizes
        }
        return sizeNames.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public String getCodeColors() {
        if (codeColors == null) {
            return null;  // Return null if no colors
        }
        return codeColors.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
