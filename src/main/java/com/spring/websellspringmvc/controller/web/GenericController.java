package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.utils.constraint.PageAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GenericController {
    @GetMapping("/about")
    public ModelAndView getAboutPage() {
        return new ModelAndView(PageAddress.ABOUT.getPage());
    }

    @GetMapping("/common/{page}")
    public String getCommon(@PathVariable("page") String page) {
        return "common/" + page;
    }

    @GetMapping("/header")
    public String getHeader() {
        return "common/header";
    }

    @GetMapping("/footer")
    public String getFooter() {
        return "common/footer";
    }

    @GetMapping("/commonLink")
    public String getCommonLink() {
        return "common/commonLink";
    }

    @GetMapping("/adminLink")
    public String getAdminLink() {
        return "common/adminLink";
    }


}
