package com.spring.websellspringmvc.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GenericController {
    @RequestMapping("/{path}")
    public String handleRequest(@PathVariable("path") String path) {
        if (path.equals("/")) {
            return "index";
        }
        return path;
    }
}
