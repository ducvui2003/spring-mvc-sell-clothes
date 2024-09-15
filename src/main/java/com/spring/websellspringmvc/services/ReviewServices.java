package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.OrderDAO;
import com.spring.websellspringmvc.dao.OrderDetailDAO;
import com.spring.websellspringmvc.dao.ReviewDAO;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.OrderDetail;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Review;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServices {
    ReviewDAO reviewDAO;
    OrderDAO orderDAO;
    OrderDetailDAO orderDetailDAO;

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
}
