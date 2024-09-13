package com.spring.websellspringmvc.filter.authorization;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.models.shoppingCart.ShoppingCart;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "user", urlPatterns = {"/public/user/*", "/api/cart/*", "/api/checkout/*", "/api/user/*", "/api/voucher/*"})
public class UserRole implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        User user = SessionManager.getInstance(httpServletRequest, httpServletResponse).getUser();
//        Chưa đăng nhập
        if (user == null) {
            httpServletResponse.sendRedirect(ConfigPage.SIGN_IN);
        } else {
            chain.doFilter(request, response);
        }
    }
}
 
