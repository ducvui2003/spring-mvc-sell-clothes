package com.spring.websellspringmvc.filter.authorization;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "admin", urlPatterns = {"/public/admin/*", "/api/admin/*"})
public class AdminRole implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        User user = SessionManager.getInstance(httpServletRequest, httpServletResponse).getUser();
        if (user == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        boolean isAdmin = user.getRole().equals("2");
        if (isAdmin) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
 
