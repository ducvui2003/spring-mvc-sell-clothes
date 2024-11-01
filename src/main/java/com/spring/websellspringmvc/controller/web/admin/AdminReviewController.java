package com.spring.websellspringmvc.controller.web.admin;

import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminReviewControllerMVC")
@RequiredArgsConstructor
@RequestMapping("/admin/review")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminReviewController {
    @GetMapping
    public ModelAndView showReviewPage() {
        ModelAndView mav = new ModelAndView(PageAddress.ADMIN_REVIEW.getPage());
        return mav;
    }
}
