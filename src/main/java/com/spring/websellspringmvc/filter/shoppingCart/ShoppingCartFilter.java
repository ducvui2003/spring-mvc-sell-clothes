package com.spring.websellspringmvc.filter.shoppingCart;

import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.ShoppingCartServices;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebFilter({"/public/user/shoppingCart.jsp"})
public class ShoppingCartFilter implements Filter {
    ShoppingCartServices shoppingCartServices;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        List<Voucher> listVouchers = shoppingCartServices.getListVouchers();
        request.setAttribute("listVouchers", listVouchers);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
