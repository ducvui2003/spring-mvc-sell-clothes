package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.ReviewDAO;
import com.spring.websellspringmvc.dao.UserDAO;
import com.spring.websellspringmvc.models.Review;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.state.ReviewState;

import java.util.List;

public class AdminReviewServices {
    public static AdminReviewServices INSTANCE;
    private static final int LIMIT = 10;
    private ReviewDAO reviewDAO;
    private UserDAO userDAO;

    private AdminReviewServices() {
        this.reviewDAO = new ReviewDAO();
        this.userDAO = new UserDAO();

    }

    public static AdminReviewServices getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new AdminReviewServices();
        return INSTANCE;
    }

    public List<Review> getReviews(int pageNumber) {
        return reviewDAO.getReviews(pageNumber, LIMIT);
    }

    public int getQuantityPage() {
        double quantityPage = Math.ceil(Double.parseDouble(reviewDAO.getQuantityProduct() + "") / LIMIT);
        return (int) quantityPage;
    }

    public int getOrderDetailId(int reviewId) {
        return reviewDAO.getOrderDetailId(reviewId);
    }

    public Review getReview(int reviewId) {
        return reviewDAO.getReviewById(reviewId);
    }

    public void updateVisibility(int reviewId, ReviewState reviewState) {
        reviewDAO.updateVisibility(reviewId, reviewState.getValue());
    }

    public User getUserByIdProductDetail(int orderDetailId) {
        List<User> listUser = userDAO.getUserByIdProductDetail(orderDetailId);
        if (listUser.isEmpty()) {
            return null;
        }
        return listUser.get(0);
    }

    public List<Review> getAll() {
        return reviewDAO.getAll();
    }
}
