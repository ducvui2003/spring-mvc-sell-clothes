package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dto.mvc.response.ReviewOverallResponse;
import com.spring.websellspringmvc.models.ReviewFilter;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface ReviewService {
  Page<ReviewOverallResponse> getReviewByProduct(ReviewFilter reviewFilter);

  Map<String, Object> calculateRating(int productId);
}
