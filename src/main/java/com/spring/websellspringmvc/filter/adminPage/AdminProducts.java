package com.spring.websellspringmvc.filter.adminPage;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.services.admin.AdminProductServices;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "adminProducts", urlPatterns = {
        "/public/admin/adminProducts.jsp", "/filterProductAdmin" ,"/public/admin/adminProductForm.jsp"
})
public class AdminProducts implements Filter {
    private final int LIMIT = 15;
    private final int DEFAULT_PAGE = 1;

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        List<Product> productCardList = AdminProductServices.getINSTANCE().getProducts(DEFAULT_PAGE);
        request.setAttribute("productCardList", productCardList);
        int quantityPage = AdminProductServices.getINSTANCE().getQuantityPage();
        request.setAttribute("quantityPage", quantityPage);
        String requestURL = "/filterProductAdmin?";
        request.setAttribute("requestURL", requestURL);

        int quantityPageMin = 1;
        int quantityPageMax = 5;
        request.setAttribute("quantityPageMin", quantityPageMin);
        request.setAttribute("quantityPageMax", quantityPageMax);
        chain.doFilter(request, response);
    }
}
 
