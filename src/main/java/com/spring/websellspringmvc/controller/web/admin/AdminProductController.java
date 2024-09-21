package com.spring.websellspringmvc.controller.web.admin;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Color;
import com.spring.websellspringmvc.models.Size;
import com.spring.websellspringmvc.services.ProductCardServices;
import com.spring.websellspringmvc.utils.MoneyRange;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller("adminProductControllerMVC")
@RequiredArgsConstructor
@RequestMapping("/admin/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminProductController {
    ProductCardServices productCardServices;

    @GetMapping
    public ModelAndView showProductPage() {
        ModelAndView modelAndView = new ModelAndView(PageAddress.ADMIN_PRODUCT.getPage());
        List<Category> categoryList = productCardServices.getAllCategory();
        List<Size> sizeList = productCardServices.getAllSize();
        List<Color> colorList = productCardServices.getAllColor();

        modelAndView.addObject("categoryList", categoryList);
        modelAndView.addObject("sizeList", sizeList);
        modelAndView.addObject("colorList", colorList);

        return modelAndView;
    }


}
