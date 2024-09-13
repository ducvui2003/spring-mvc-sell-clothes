package com.spring.websellspringmvc.filter.product;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.services.ProductCardServices;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "productBuying", urlPatterns = {"/filterProductBuying", "/public/product/productBuying.jsp"})
public class ProductBuying implements Filter {
    private final int DEFAULT_PAGE = 1;

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        List<Product> productCardList = ProductCardServices.getINSTANCE().getProducts(DEFAULT_PAGE);
        request.setAttribute("productCardList", productCardList);
        int quantityPage = ProductCardServices.getINSTANCE().getQuantityPage();
        request.setAttribute("quantityPage", quantityPage);
        String requestURL = "/filterProductBuying?";
        request.setAttribute("requestURL", requestURL);
        request.setAttribute("quantityPageMin", 1);
        request.setAttribute("quantityPageMax", 5);
        chain.doFilter(request, response);
    }
}
 
