package com.spring.websellspringmvc.filter;

import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.models.shoppingCart.ShoppingCart;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class CheckAccountFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        User user = SessionManager.getInstance((HttpServletRequest) request, (HttpServletResponse) response).getUser();
//
//        if (user != null) {
//            HttpSession session = ((HttpServletRequest) request).getSession();
//            if (session.getAttribute(user.getId() + "") == null) {
//                session.setAttribute(user.getId() + "", new ShoppingCart());
//            }
//        }
//        chain.doFilter(request, response);
    }
}
