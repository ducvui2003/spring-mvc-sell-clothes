package com.spring.websellspringmvc.controller.api.authentication;


import com.spring.websellspringmvc.services.authentication.AuthenticateServices;
import com.spring.websellspringmvc.utils.Validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name = "usernameExists", value = "/usernameExists")
public class UsernameExists extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        if(username == null)
            username = "";
        Validation validation = AuthenticateServices.getINSTANCE().checkUsernameExists(username);
//        If there are warnings
        if (validation.getFieldUsername() != null && !validation.getFieldUsername().isEmpty()) {
            String usernameError = validation.getFieldUsername();
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(usernameError);
        }
    }
}
