package com.spring.websellspringmvc.controller.api.authentication;


import com.spring.websellspringmvc.services.authentication.AuthenticateServices;
import com.spring.websellspringmvc.utils.Validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "emailExists", value = "/emailExists")
public class EmailExists extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if(email == null)
            email = "";

        Validation validation = AuthenticateServices.getINSTANCE().checkEmailExists(email);
//        If there are warnings
        if (validation.getFieldEmail() != null && !validation.getFieldEmail().isEmpty()) {
            String emailError = validation.getFieldEmail();
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(emailError);
        }
    }
}
