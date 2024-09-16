package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Slider;
import com.spring.websellspringmvc.services.HomeServices;
import jakarta.servlet.RequestDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    HomeServices homeServices;

    @RequestMapping({"/", "/home"})
    ModelAndView homePage() {
        ModelAndView mav = new ModelAndView("index");
        List<Slider> listSlideShow = homeServices.getListSlideShow();
        List<Product> list6NewProducts = homeServices.getListNewProducts(false);
        List<Product> list6TrendProducts = homeServices.getListTrendProducts(false);
        List<Product> listAllTrendingProducts = homeServices.getListTrendProducts(false);

        mav.addObject("listSlideShow", listSlideShow);
        mav.addObject("list6TrendingProducts", list6TrendProducts);
        mav.addObject("list6NewProducts", list6NewProducts);
        mav.addObject("listAllTrendingProducts", listAllTrendingProducts);
        return mav;
    }

    @GetMapping("/trendingProducts")
    ModelAndView trendingPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        ModelAndView mav = new ModelAndView(ConfigPage.PRODUCT_TRENDING);
        List<Product> listAllTrendingProducts = homeServices.getListTrendProducts(true);
        int itemsPerPage = 8;
        int size = listAllTrendingProducts.size();
        int totalPage = (size % itemsPerPage == 0 ? (size / itemsPerPage) : ((size / itemsPerPage)) + 1);

        int start, end;
        start = (page - 1) * itemsPerPage;
        end = Math.min(page * itemsPerPage, size);
        List<Product> listProductsPerPage = getListProductsPerPage(listAllTrendingProducts, start, end);

        mav.addObject("page", page);
        mav.addObject("totalPage", totalPage);
        mav.addObject("listProductsPerPage", listProductsPerPage);
        log.info("listProductsPerPage: {}", listProductsPerPage);

        return mav;
    }

    @GetMapping("/newProducts")
    ModelAndView newProductsPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        ModelAndView mav = new ModelAndView(ConfigPage.PRODUCT_NEW);
        List<Product> listAllNewProducts = homeServices.getListNewProducts(true);
        int itemsPerPage = 8;
        int size = listAllNewProducts.size();
        int totalPage = (size % itemsPerPage == 0 ? (size / itemsPerPage) : ((size / itemsPerPage)) + 1);

        int start, end;
        start = (page - 1) * itemsPerPage;
        end = Math.min(page * itemsPerPage, size);
        List<Product> listProductsPerPage = getListProductsPerPage(listAllNewProducts, start, end);

        mav.addObject("page", page);
        mav.addObject("totalPage", totalPage);
        mav.addObject("listProductsPerPage", listProductsPerPage);

        return mav;
    }


    private List<Product> getListProductsPerPage(List<Product> listProducts, int start, int end) {
        List<Product> listProductsPerPage = new ArrayList<>();
        for (int i = start; i < end; i++) {
            listProductsPerPage.add(listProducts.get(i));
        }
        return listProductsPerPage;
    }
}
