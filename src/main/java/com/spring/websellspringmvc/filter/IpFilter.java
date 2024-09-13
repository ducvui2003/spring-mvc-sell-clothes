package com.spring.websellspringmvc.filter;

import com.spring.websellspringmvc.services.LogService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class IpFilter implements Filter {
    @Override

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = request.getRemoteHost();
        if (!ip.equals("0:0:0:0:0:0:0:1")) {
            LogService.getINSTANCE().setIp(ip);
        }
        chain.doFilter(request, response);

    }
}
