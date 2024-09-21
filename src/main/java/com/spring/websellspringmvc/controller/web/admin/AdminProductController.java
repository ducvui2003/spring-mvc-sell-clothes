package com.spring.websellspringmvc.controller.web.admin;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminProductControllerMVC")
@RequiredArgsConstructor
@RequestMapping("/admin/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminProductController {
    @GetMapping
    public ModelAndView showProductPage() {
        return new ModelAndView(PageAddress.ADMIN_PRODUCT.getPage());
    }
}
