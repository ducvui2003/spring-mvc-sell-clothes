package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dao.OrderDetailDAO;
import com.spring.websellspringmvc.dao.ReviewDAO;
import com.spring.websellspringmvc.dto.mvc.response.ReviewOverallResponse;
import com.spring.websellspringmvc.mapper.ReviewMapper;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import com.spring.websellspringmvc.utils.constraint.KeyAttribute;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {
    ReviewDAO reviewDAO;
    OrderDAO orderDAO;
    OrderDetailDAO orderDetailDAO;
    ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    CloudinaryUploadServices cloudinaryUploadServices;

    public boolean canReview(int userId, int orderProductIdRequest) {
        List<Review> listReview = reviewDAO.checkReview(userId, orderProductIdRequest);
        return listReview.isEmpty();
    }

    public String getNameProduct(int orderProductId) {
        List<Product> listProduct = reviewDAO.getNameProduct(orderProductId);
        return listProduct.isEmpty() ? null : listProduct.get(0).getName();
    }

    public List<Review> getListReview(int productId) {
        return reviewDAO.getReviewByProductId(productId, true);
    }

    public boolean hasReview(int orderDetailId) {
        return reviewDAO.getReviewByOrderDetailId(orderDetailId).isEmpty();
    }

    public OrderDetail getOrderDetail(int orderDetailId) {
        return orderDetailDAO.getOrderDetailById(orderDetailId).get(0);
    }

    public void createReview(Review review) {
        reviewDAO.createReview(review);
    }

    public Order getOrder(String orderId) {
        return orderDAO.getOrderById(orderId);
    }

    @Override
    public Page<ReviewOverallResponse> getReviewByProduct(ReviewFilter productFilter) {
        List<Review> reviews = reviewDAO.getReviewsByProductId(productFilter);
        List<ReviewOverallResponse> response = new ArrayList<>();

        for (Review review : reviews) {
            ReviewOverallResponse reviewOverallResponse = reviewMapper.toReviewOverallResponse(review);
            Optional<User> userOptional = orderDetailDAO.getUserByIdProductDetail(review.getOrderDetailId());
            userOptional.ifPresent(user -> reviewOverallResponse.setName(user.getFullName()));
            userOptional.ifPresent(user -> reviewOverallResponse.setAvatar(cloudinaryUploadServices.getImage(ImagePath.USER.getPath(), user.getAvatar())));
            response.add(reviewOverallResponse);
        }
        long total = reviewDAO.countFilter(productFilter);
        return new PageImpl<>(response, productFilter.getPageable(), total);
    }

    @Override
    public Map<String, Object> calculateRating(int productId) {
        List<Review> list = reviewDAO.getReviewStar(productId);
        if (list.isEmpty()) return Map.of();
        int totalStar = 0;
        for (Review item : list) {
            totalStar += item.getRatingStar();
        }
        return Map.of(KeyAttribute.RATING_STAR.name(), totalStar / list.size(), KeyAttribute.REVIEW_COUNT.name(), list.size());
    }
}
