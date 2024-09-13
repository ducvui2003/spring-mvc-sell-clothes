package com.spring.websellspringmvc.filter.home;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Slider;
import com.spring.websellspringmvc.services.HomeServices;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = {"/", "/index", ""})
public class Home implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        List<Product> listAllTrendingProducts = HomeServices.getINSTANCE().getListTrendProducts(true);

        List<Slider> listSlideShow = HomeServices.getINSTANCE().getListSlideShow();
        List<Product> list6NewProducts = HomeServices.getINSTANCE().getListNewProducts(false);
        List<Product> list6TrendProducts = HomeServices.getINSTANCE().getListTrendProducts(false);
        request.setAttribute("listSlideShow", listSlideShow);
        request.setAttribute("list6TrendingProducts", list6TrendProducts);
        request.setAttribute("list6NewProducts", list6NewProducts);
        request.setAttribute("listAllTrendingProducts", listAllTrendingProducts);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
