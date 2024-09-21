package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.controller.exception.ResourceNotFoundException;
import com.spring.websellspringmvc.models.OrderDetail;
import com.spring.websellspringmvc.models.Parameter;
import com.spring.websellspringmvc.models.Review;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.ProductCardServices;
import com.spring.websellspringmvc.services.ReviewServicesImpl;
import com.spring.websellspringmvc.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewServicesImpl reviewServicesImpl;
    ProductCardServices productCardServices;
    SessionManager sessionManager;

    @PostMapping("/createReview")
    public ModelAndView createReview(@RequestParam("orderProductId") int orderProductId,
                                     @RequestParam(value = "ratingStar", defaultValue = "5") int ratingStar,
                                     @RequestParam("desc") String desc,
                                     HttpServletRequest request, HttpServletResponse response
    ) {
        ModelAndView mav = new ModelAndView(ConfigPage.USER_REVIEW_SUCCESS);

        User user = sessionManager.getUser();
        int userId = user.getId();

        if (!reviewServicesImpl.canReview(userId, orderProductId)) {
            throw new ResourceNotFoundException();
        }
        Review review = new Review();
        review.setOrderDetailId(orderProductId);
        review.setRatingStar(ratingStar);
        review.setFeedback(desc);
        review.setReviewDate(Date.valueOf(LocalDate.now()));
        reviewServicesImpl.createReview(review);

        String nameProduct = reviewServicesImpl.getNameProduct(orderProductId);
        if (nameProduct == null)
            throw new ResourceNotFoundException();

        mav.addObject("nameProduct", nameProduct);
        return mav;
    }

    @GetMapping("/review")
    public ModelAndView reviewPage(@RequestParam("orderDetailId") int orderDetailId) {
        ModelAndView mav = new ModelAndView(ConfigPage.USER_REVIEW);
//        Check
        boolean listReview = reviewServicesImpl.hasReview(orderDetailId);
        if (!listReview)
            throw new ResourceNotFoundException();


        OrderDetail orderDetail = reviewServicesImpl.getOrderDetail(orderDetailId);

        String color = orderDetail.getColorRequired();
        String[] sizes = readSizes(orderDetail.getSizeRequired());
        int quantity = orderDetail.getQuantityRequired();
        int productId = orderDetail.getProductId();
        String nameProduct = productCardServices.getNameProductById(productId);
        List<Parameter> listParameter = productCardServices.getParameterByIdCategory(productId);
        mav.addObject("orderDetailId", orderDetailId);
        mav.addObject("productId", productId);
        mav.addObject("nameProduct", nameProduct);
        mav.addObject("listParameter", listParameter);
        mav.addObject("color", color);
        mav.addObject("sizes", sizes);
        mav.addObject("quantity", quantity);
        return mav;
    }

    private String[] readSizes(String sizesRequired) {
//        Dài áo: 70 cm, Dài tay: 22 cm, Rộng gấu: 54 cm, Rộng bắp tay: 24 cm, Rộng vai: 50 cm
        return sizesRequired.split(", ");
    }
}
