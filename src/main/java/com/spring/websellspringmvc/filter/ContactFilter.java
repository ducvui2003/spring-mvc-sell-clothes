package com.spring.websellspringmvc.filter;

import com.spring.websellspringmvc.models.SubjectContact;
import com.spring.websellspringmvc.services.ContactServices;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter("/public/contact.jsp")
public class ContactFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding("UTF-8");
        List<SubjectContact> listContactSubjects = ContactServices.getINSTANCE().getListContactSubjects();
        request.setAttribute("listContactSubjects", listContactSubjects);
        filterChain.doFilter(request, response);
    }
}
