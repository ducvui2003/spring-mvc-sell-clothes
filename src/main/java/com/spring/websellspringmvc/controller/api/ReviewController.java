package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.ApiPaging;
import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.dto.mvc.response.ReviewOverallResponse;
import com.spring.websellspringmvc.models.ProductFilter;
import com.spring.websellspringmvc.models.ReviewFilter;
import com.spring.websellspringmvc.services.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("apiReviewController")
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiPaging<ReviewOverallResponse>> getReviewByProduct(
            @PathVariable("productId") Integer productId,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "9", required = false) Integer size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        ReviewFilter reviewFilter = ReviewFilter.of(pageable, productId);
        Page<ReviewOverallResponse> reviewOverallPage = reviewService.getReviewByProduct(reviewFilter);

        return ResponseEntity.ok(ApiPaging.<ReviewOverallResponse>builder()
                .content(reviewOverallPage.getContent())
                .page(reviewOverallPage.getNumber())
                .size(reviewOverallPage.getSize())
                .totalElement(reviewOverallPage.getTotalElements())
                .totalPage(reviewOverallPage.getTotalPages())
                .build());
    }
}
