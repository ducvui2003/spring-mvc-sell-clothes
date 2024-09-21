package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Slider;
import com.spring.websellspringmvc.services.HomeServices;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import jakarta.servlet.RequestDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    HomeServices homeServices;
    CloudinaryUploadServices cloudinaryUploadServices;

    @RequestMapping({"/", "/home"})
    ModelAndView homePage() {
        ModelAndView mav = new ModelAndView("index");
        List<Slider> listSlideShow = homeServices.getListSlideShow();
        List<ProductCardResponse> list6NewProducts = homeServices.getListNewProducts(false);
        Page<ProductCardResponse> list6TrendProducts = homeServices.getListTrendProducts(PageRequest.of(0, 6));

        Map<String, String> categorySlider = new HashMap<>();
        categorySlider.put("Áo thun / T-Shirt", cloudinaryUploadServices.getImage("category", "T-shirt"));
        categorySlider.put("Áo hoodie / Hoodie", cloudinaryUploadServices.getImage("category", "hoodie"));
        categorySlider.put("Balo / Backpack", cloudinaryUploadServices.getImage("category", "backpack"));
        categorySlider.put("Quần dài / Pants", cloudinaryUploadServices.getImage("category", "pants"));
        categorySlider.put("Nón / Cap", cloudinaryUploadServices.getImage("category", "hat"));

        List<String> listSlider = listSlideShow.stream().map(slider ->
                cloudinaryUploadServices.getImage("slider/", slider.getNameImage())
        ).toList();

        Map<String, String> stepGuide = new HashMap<>();
        stepGuide.put("1", cloudinaryUploadServices.getImage("step_guide", "choose"));
        stepGuide.put("2", cloudinaryUploadServices.getImage("step_guide", "customize"));
        stepGuide.put("3", cloudinaryUploadServices.getImage("step_guide", "checkout"));
        stepGuide.put("4", cloudinaryUploadServices.getImage("step_guide", "receive"));


        mav.addObject("categorySlider", categorySlider);
        mav.addObject("listSlider", listSlider);
        mav.addObject("list6TrendingProducts", list6TrendProducts.getContent());
        mav.addObject("list6NewProducts", list6NewProducts);
        mav.addObject("stepGuide", stepGuide);
        return mav;
    }

    @GetMapping("/trendingProducts")
    ModelAndView trendingPage(@PageableDefault(page = 1, size = 10) Pageable pagable) {
        ModelAndView mav = new ModelAndView(PageAddress.PRODUCT_TRENDING.getPage());
        Page<ProductCardResponse> page = homeServices.getListTrendProducts(pagable);

        mav.addObject("page", pagable.getPageNumber());
        mav.addObject("totalPage", page.getTotalPages());
        mav.addObject("content", page.getContent());
        log.info("listProductsPerPage: {}", page.getContent());
        return mav;
    }

    @GetMapping("/newProducts")
    ModelAndView newProductsPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        ModelAndView mav = new ModelAndView(ConfigPage.PRODUCT_NEW);
//        List<Product> listAllNewProducts = homeServices.getListNewProducts(true);
//        int itemsPerPage = 8;
//        int size = listAllNewProducts.size();
//        int totalPage = (size % itemsPerPage == 0 ? (size / itemsPerPage) : ((size / itemsPerPage)) + 1);
//
//        int start, end;
//        start = (page - 1) * itemsPerPage;
//        end = Math.min(page * itemsPerPage, size);
//        List<Product> listProductsPerPage = getListProductsPerPage(listAllNewProducts, start, end);
//
//        mav.addObject("page", page);
//        mav.addObject("totalPage", totalPage);
//        mav.addObject("listProductsPerPage", listProductsPerPage);

        return mav;
    }

}
