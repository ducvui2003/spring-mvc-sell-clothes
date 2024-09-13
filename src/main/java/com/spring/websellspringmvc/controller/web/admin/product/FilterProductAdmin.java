package com.spring.websellspringmvc.controller.web.admin.product;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.utils.FilterStrategy;
import com.spring.websellspringmvc.utils.FilterStrategyAdmin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "filterProductAdmin", value = "/filterProductAdmin")
public class FilterProductAdmin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FilterStrategy filterStrategy = new FilterStrategyAdmin(request, response);
        if (!filterStrategy.isAllParameterEmpty()) {
            filterStrategy.doFilter();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
