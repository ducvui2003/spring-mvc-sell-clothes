package com.spring.websellspringmvc.controller.web.product;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.utils.FilterStrategy;
import com.spring.websellspringmvc.utils.FilterStrategyBuying;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "filterProductBuying", value = "/filterProductBuying")
public class FilterProductBuying extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        FilterStrategy filterStrategy = new FilterStrategyBuying(request, response);
//        if (!filterStrategy.isAllParameterEmpty()) {
//            filterStrategy.doFilter();
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }
}