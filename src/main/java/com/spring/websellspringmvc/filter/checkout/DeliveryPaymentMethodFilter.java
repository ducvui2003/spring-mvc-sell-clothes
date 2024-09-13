package com.spring.websellspringmvc.filter.checkout;

import com.spring.websellspringmvc.models.shoppingCart.ShoppingCart;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter("/*")
public class DeliveryPaymentMethodFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getServletPath();

        HttpSession session = request.getSession(true);
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        if(!requestURI.contains("Checkout") && !requestURI.contains("checkout.jsp")){
//            if(cart != null && cart.getDeliveryMethod() != null){
//                cart.setDeliveryMethod(null);
//                session.setAttribute("cart", cart);
//            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
