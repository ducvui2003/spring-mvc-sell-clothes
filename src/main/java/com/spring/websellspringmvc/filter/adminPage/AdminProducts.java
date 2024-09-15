package com.spring.websellspringmvc.filter.adminPage;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.services.admin.AdminProductServices;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "adminProducts", urlPatterns = {
        "/public/admin/adminProducts.jsp", "/filterProductAdmin", "/public/admin/adminProductForm.jsp"
})
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminProducts implements Filter {

    int LIMIT = 15;
    int DEFAULT_PAGE = 1;
    AdminProductServices adminProductServices;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        List<Product> productCardList = adminProductServices.getProducts(DEFAULT_PAGE);
        request.setAttribute("productCardList", productCardList);
        int quantityPage = adminProductServices.getQuantityPage();
        request.setAttribute("quantityPage", quantityPage);
        String requestURL = "/filterProductAdmin?";
        request.setAttribute("requestURL", requestURL);

        int quantityPageMin = 1;
        int quantityPageMax = 5;
        request.setAttribute("quantityPageMin", quantityPageMin);
        request.setAttribute("quantityPageMax", quantityPageMax);
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
 
