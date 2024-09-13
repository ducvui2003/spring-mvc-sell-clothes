package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Slider;
import com.spring.websellspringmvc.services.HomeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @RequestMapping({"/", "/home"})
    ModelAndView home() {
        ModelAndView mav = new ModelAndView("index");
        List<Slider> listSlideShow = HomeServices.getINSTANCE().getListSlideShow();
        List<Product> list6NewProducts = HomeServices.getINSTANCE().getListNewProducts(false);
        List<Product> list6TrendProducts = HomeServices.getINSTANCE().getListTrendProducts(false);
        List<Product> listAllTrendingProducts = HomeServices.getINSTANCE().getListTrendProducts(true);

        mav.addObject("listSlideShow", listSlideShow);
        mav.addObject("list6TrendingProducts", list6TrendProducts);
        mav.addObject("list6NewProducts", list6NewProducts);
        mav.addObject("listAllTrendingProducts", listAllTrendingProducts);
        return mav;
    }
}
