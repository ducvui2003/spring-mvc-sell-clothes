package com.spring.websellspringmvc.controller.web.authentication;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.services.authentication.AuthenticateServices;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "resetPassword", value = "/resetPassword")
public class ResetPassword extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String tokenResetPassword = request.getParameter("token-reset-password");
        boolean status = AuthenticateServices.getINSTANCE().resetPassword(email, tokenResetPassword);
        if (status) {
            request.setAttribute("email", email);
            request.setAttribute("token", tokenResetPassword);
            request.getRequestDispatcher(ConfigPage.RESET_PASSWORD).forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}