package com.spring.websellspringmvc.controller.web.authentication;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.GoogleUser;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.authentication.AuthenticateServices;
import com.spring.websellspringmvc.services.authentication.GoogleLoginServices;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "signInGoogle", value = "/signInGoogle")
public class SignInGoogle extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            RequestDispatcher dis = request.getRequestDispatcher(ConfigPage.HOME);
            dis.forward(request, response);
        } else {
            String accessToken = GoogleLoginServices.getINSTANCE().getToken(code);
            GoogleUser googleUserAccount = (GoogleUser) GoogleLoginServices.getINSTANCE().getUserInfo(accessToken);
            String emailGoogle = googleUserAccount.getEmail();
            User userValid = AuthenticateServices.getINSTANCE().checkSignIn(emailGoogle);
            if (userValid != null) {
//                Tài khoản đã tồn tại
                SessionManager.getInstance(request, response).addUser(userValid);
                response.sendRedirect(ConfigPage.HOME);
            } else {
//                Tài khoản chưas tồn tại
                request.setAttribute("email", emailGoogle);
                RequestDispatcher dis = request.getRequestDispatcher(ConfigPage.SIGN_UP);
                dis.forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}