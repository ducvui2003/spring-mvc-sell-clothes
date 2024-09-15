package com.spring.websellspringmvc.dto.response;

import com.spring.websellspringmvc.models.Product;
import lombok.Data;

import java.util.Map;

@Data
public class DashBoardDetailResponse {
    private Long orderSuccess;
    private Long orderFailed;
    private Double revenue;
    private Map<Product, Integer> productPopular;
    private Map<Product, Integer> productNotPopular;
}
