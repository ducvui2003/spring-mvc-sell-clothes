package com.spring.websellspringmvc.filter.adminPage;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "AdminLogs", urlPatterns = {"/public/admin/adminLogs.jsp"})
public class AdminLogs implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}
