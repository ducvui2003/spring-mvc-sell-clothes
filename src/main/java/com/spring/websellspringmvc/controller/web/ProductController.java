package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.controller.exception.ResourceNotFoundException;
import com.spring.websellspringmvc.dto.mvc.response.ProductDetailResponse;
import com.spring.websellspringmvc.services.ProductService;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @GetMapping("/product")
    public ModelAndView filterProductBuying() {
        return new ModelAndView(PageAddress.PRODUCT_BUYING.getPage());
    }


    @GetMapping("/product/{id}")
    public ModelAndView showProductDetail(@PathVariable(value = "id") int id) {
        ModelAndView mav = new ModelAndView(PageAddress.PRODUCT_DETAIL.getPage());

        ProductDetailResponse product = productService.getProductDetail(id);
        if (product == null) {
            throw new ResourceNotFoundException();
        }
        mav.addObject("product", product);
        return mav;
    }
}
