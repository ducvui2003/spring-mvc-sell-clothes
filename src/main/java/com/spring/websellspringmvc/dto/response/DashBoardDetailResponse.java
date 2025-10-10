package com.spring.websellspringmvc.dto.response;

import com.spring.websellspringmvc.models.Product;
import java.util.Map;
import lombok.Data;

@Data
public class DashBoardDetailResponse {
  private Long orderSuccess;
  private Long orderFailed;
  private Double revenue;
  private Map<Product, Integer> productPopular;
  private Map<Product, Integer> productNotPopular;
}
