package com.spring.websellspringmvc.mapper;

import com.spring.websellspringmvc.dto.mvc.response.ReviewOverallResponse;
import com.spring.websellspringmvc.dto.response.ReviewDetailResponse;
import com.spring.websellspringmvc.dto.response.datatable.ReviewDatatable;
import com.spring.websellspringmvc.models.Review;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    ReviewDetailResponse.ReviewResponse toReviewResponse(Review review);

    ReviewDatatable toReviewDatatableResponse(Review review);

    ReviewOverallResponse toReviewOverallResponse(Review review);
}
