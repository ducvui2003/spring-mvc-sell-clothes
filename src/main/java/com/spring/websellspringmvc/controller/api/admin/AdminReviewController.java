package com.spring.websellspringmvc.controller.api.admin;

import com.google.gson.JsonObject;
import com.spring.websellspringmvc.controller.exception.ResourceNotFoundException;
import com.spring.websellspringmvc.dto.response.ReviewDetailResponse;
import com.spring.websellspringmvc.mapper.OrderMapper;
import com.spring.websellspringmvc.mapper.ReviewMapper;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.OrderDetail;
import com.spring.websellspringmvc.models.Review;
import com.spring.websellspringmvc.services.ReviewServicesImpl;
import com.spring.websellspringmvc.services.admin.AdminReviewServices;
import com.spring.websellspringmvc.services.state.ReviewState;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/review")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminReviewController {
    AdminReviewServices adminReviewServices;
    ReviewServicesImpl reviewServicesImpl;

    ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    OrderMapper orderMapper = OrderMapper.INSTANCE;

//    @PostMapping("/datatable")
//    public ResponseEntity<DatatableResponse<ReviewDatatableResponse>> getDatatable(@RequestBody DatatableRequest request) {
//        List<Review> listReview = adminReviewServices.getReviews(currentPage);
//
//        List<ReviewDatatableResponse> reviewResponses = new ArrayList<>();
//        for (Review review : listReview) {
//            int userId = -1;
//            if (UserFactory.getUserByIdProductDetail(review.getOrderDetailId()) != null) {
//                userId = UserFactory.getUserByIdProductDetail(review.getOrderDetailId()).getId();
//            }
//            ReviewDatatableResponse reviewResponse = reviewMapper.toReviewDatatableResponse(review);
//            reviewResponse.setProductName(ProductFactory.getNameProductByIdOrderDetail(review.getOrderDetailId()));
//            reviewResponses.add(reviewResponse);
//        }
//
//        int quantityPageTotal = AdminReviewServices.getINSTANCE().getQuantityPage();
//        ReviewDatatableController.ListReviewResponse listReviewResponse = new ReviewDatatableController.ListReviewResponse(quantityPageTotal, reviewResponses);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("quantity", quantityPageTotal);
//        jsonObject.put("reviews", listReviewResponse);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonResponse = null;
//        try {
//            jsonResponse = mapper.writeValueAsString(listReviewResponse);
//            response.getWriter().write(jsonResponse);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @GetMapping("/detail")
    public ResponseEntity<ReviewDetailResponse> getReview(@RequestParam("id") Integer reviewId) {
        if (reviewId == null)
            throw new ResourceNotFoundException();
        Review review = adminReviewServices.getReview(reviewId);
        if (review == null)
            throw new ResourceNotFoundException();

        OrderDetail orderDetail = reviewServicesImpl.getOrderDetail(review.getOrderDetailId());
        Order order = reviewServicesImpl.getOrder(orderDetail.getOrderId());

        return ResponseEntity.ok(ReviewDetailResponse.builder().order(orderMapper.toOrderResponse(order))
                .orderDetail(orderMapper.toOrderDetailResponse(orderDetail))
                .review(reviewMapper.toReviewResponse(review)).build());
    }

    @PutMapping("/visible")
    public ResponseEntity<?> updateVisibility(@RequestParam("id") Integer reviewId, @RequestParam("type") ReviewState state) {
        JsonObject jsonObject = new JsonObject();
        if (reviewId == null || state == null) {
            jsonObject.addProperty("success", false);
            return ResponseEntity.badRequest().body(jsonObject.toString());
        }
        try {
            //Chưa case TH nếu không tìm thấy review id
            adminReviewServices.updateVisibility(reviewId, state);
            jsonObject.addProperty("success", true);
        } catch (NumberFormatException e) {
            jsonObject.addProperty("success", false);
        }
        return ResponseEntity.badRequest().body(jsonObject.toString());
    }
}
