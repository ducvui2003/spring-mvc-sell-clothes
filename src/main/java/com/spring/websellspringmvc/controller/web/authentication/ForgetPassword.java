package com.spring.websellspringmvc.controller.web.authentication;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.authentication.AuthenticateServices;
import com.spring.websellspringmvc.utils.Validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "forgetPassword", value = "/forgetPassword")
public class ForgetPassword extends HttpServlet {

    String sendMailSuccess = "Email đã được gửi đến hộp thư của bạn";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        Validation validation = AuthenticateServices.getINSTANCE().checkForgetPassword(email);

        if (validation.getObjReturn() != null) {
            User user = (User) validation.getObjReturn();
            request.setAttribute("sendMail", sendMailSuccess);
            AuthenticateServices.getINSTANCE().sendMailResetPassword(user);
        } else {
            request.setAttribute("emailError", validation.getFieldEmail());
        }
        request.getRequestDispatcher(ConfigPage.FORGET_PASSWORD).forward(request, response);
    }
}