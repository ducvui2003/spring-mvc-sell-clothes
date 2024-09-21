package com.spring.websellspringmvc.controller.web.admin;

import com.spring.websellspringmvc.services.admin.DashboardServices;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("dashBoardControllerMVC")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashBoardController {
    DashboardServices dashboardServices;

    @GetMapping("/admin/dashboard")
    public ModelAndView getDashBoard() {
        ModelAndView mav = new ModelAndView(PageAddress.ADMIN_DASHBOARD.getPage());
        int quantityUser = dashboardServices.countUser();
        int quantityProduct = dashboardServices.countProduct();
        int quantityOrder = dashboardServices.countOrder();
        int quantityReview = dashboardServices.countReview();

        mav.addObject("user", quantityUser);
        mav.addObject("product", quantityProduct);
        mav.addObject("order", quantityOrder);
        mav.addObject("review", quantityReview);
        return mav;
    }
}
