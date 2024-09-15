package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.controller.exception.ResourceNotFoundException;
import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Review;
import com.spring.websellspringmvc.services.ProductCardServices;
import com.spring.websellspringmvc.services.ProductServices;
import com.spring.websellspringmvc.services.ReviewServices;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductCardServices productCardServices;
    ReviewServices reviewServices;
    ProductServices productServices;

    @GetMapping("/showProductDetail")
    public ModelAndView showProductDetail(@RequestParam(value = "id", defaultValue = "1") int id) {
        ModelAndView mav = new ModelAndView(ConfigPage.PRODUCT_DETAIL);

        Product product = productServices.getProductByProductId(id);
        if (product == null)
            throw new ResourceNotFoundException();
//            Product
        mav.addObject("product", product);
        //Reviews
        List<Review> listReview = reviewServices.getListReview(id);
        mav.addObject("listReview", listReview);
//            Related product
        List<Product> listProductRelated = productCardServices.getProductByCategoryId(product.getCategoryId(), 4, false);
        mav.addObject("listProductRelated", listProductRelated);
        return mav;
    }

    @GetMapping("/showProductOrder")
    public ModelAndView showProductOrder(@RequestParam(value = "id", defaultValue = "1") int id) {
        ModelAndView mav = new ModelAndView(ConfigPage.PRODUCT_ORDER);

        Product product = productServices.getProductByProductId(id);
        if (product == null)
            throw new ResourceNotFoundException();

//            Category
        Category category = productCardServices.getCategoryById(id);
//            Parameter
        List<Parameter> listParameter = productCardServices.getParameterByIdCategory(id);
//            Product
        mav.addObject("product", product);
        mav.addObject("category", category);
        mav.addObject("listParameter", listParameter);
        return mav;
    }

//    @GetMapping("/filterProductBuying")

}
