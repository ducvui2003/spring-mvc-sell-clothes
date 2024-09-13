package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.OrderDaoAdmin;
import com.spring.websellspringmvc.dao.OrderDetailDAO;
import com.spring.websellspringmvc.dao.ReviewDAO;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.OrderDetail;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Review;

import java.util.List;

public class ReviewServices {
    private static ReviewServices INSTANCE;
    private ReviewDAO reviewDAO;
    private OrderDaoAdmin orderDAO;

    private OrderDetailDAO orderDetailDAO;
    private ReviewServices() {
        reviewDAO = new ReviewDAO();
        orderDetailDAO = new OrderDetailDAO();
        orderDAO = new OrderDaoAdmin();
    }
    public static ReviewServices getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new ReviewServices();
        return INSTANCE;
    }

    public boolean canReview(int userId, int orderProductIdRequest) {
        List<Review> listReview = reviewDAO.checkReview(userId, orderProductIdRequest);
        return listReview.isEmpty() ;
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

    public void createReview(Review review){
        reviewDAO.createReview(review);
    }

    public Order getOrder(String orderId) {
        return orderDAO.getOrderById(orderId);
    }
}
