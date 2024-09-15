package com.spring.websellspringmvc.controller.web.admin;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.services.admin.DashboardServices;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("webDashBoardController")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashBoardController {
    DashboardServices dashboardServices;

    @GetMapping("/admin/dashboard")
    public ModelAndView getDashBoard() {
        ModelAndView mav = new ModelAndView(ConfigPage.DASHBOARD);
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
