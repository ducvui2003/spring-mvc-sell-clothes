package com.spring.websellspringmvc.controller.api.admin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.controller.exception.ResourceNotFoundException;
import com.spring.websellspringmvc.dto.response.ReviewDetailResponse;
import com.spring.websellspringmvc.mapper.OrderMapper;
import com.spring.websellspringmvc.mapper.ReviewMapper;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.OrderDetail;
import com.spring.websellspringmvc.models.Review;
import com.spring.websellspringmvc.services.ReviewServices;
import com.spring.websellspringmvc.services.admin.AdminReviewServices;
import com.spring.websellspringmvc.services.state.ReviewState;
import com.spring.websellspringmvc.utils.ProductFactory;
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
    ReviewServices reviewServices;

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

        OrderDetail orderDetail = reviewServices.getOrderDetail(review.getOrderDetailId());
        Order order = reviewServices.getOrder(orderDetail.getOrderId());
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.add("review", convertReview(review));
//        jsonObject.add("orderDetail", convertOrderDetail(orderDetail));
//        jsonObject.add("order", convertOrder(order));

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

    private JsonElement convertReview(Review review) {
        JsonObject result = new JsonObject();
        result.addProperty("id", review.getId());
        result.addProperty("ratingStar", review.getRatingStar());
        result.addProperty("feedback", review.getFeedback());
        result.addProperty("date", review.getReviewDate().toString());
        result.addProperty("visibility", review.isVisibility());
        return result;
    }

    private JsonElement convertOrderDetail(OrderDetail orderDetail) {
        String image = ProductFactory.getListImagesByProductId(orderDetail.getProductId()).get(0).getNameImage();
        JsonObject result = new JsonObject();
        result.addProperty("productName", orderDetail.getProductName());
        result.addProperty("image", image);
        result.addProperty("size", orderDetail.getSizeRequired());
        result.addProperty("color", orderDetail.getColorRequired());
        result.addProperty("quantity", orderDetail.getQuantityRequired());
        result.addProperty("price", orderDetail.getPrice());
        return result;
    }

    private JsonElement convertOrder(Order order) {
        JsonObject result = new JsonObject();
        result.addProperty("fullName", order.getFullName());
        result.addProperty("email", order.getEmail());
        result.addProperty("phone", order.getPhone());
        result.addProperty("province", order.getProvince());
        result.addProperty("district", order.getDistrict());
        result.addProperty("ward", order.getWard());
        result.addProperty("detail", order.getDetail());
        return result;
    }
}
