package com.spring.websellspringmvc.filter.user;

import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import jakarta.servlet.*;

import java.io.IOException;

//@WebFilter(filterName = "AccountFilter", urlPatterns = {"/public/user/accountInfo.jsp", "/api/user/*"})
public class AccountFilter implements Filter {
    CloudinaryUploadServices cloudinaryUploadServices ;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//        User user = SessionManager.getInstance(req, resp).getUser();
//        request.setAttribute("accountInfo", user);
//        String avatarLink = cloudinaryUploadServices.getImage("user/", user.getAvatar());
//        request.setAttribute("avatarLink", avatarLink);
//        chain.doFilter(request, response);
    }
}
