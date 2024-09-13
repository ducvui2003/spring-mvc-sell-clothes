package com.spring.websellspringmvc.controller.web.authentication;

import com.restfb.exception.FacebookException;
import com.restfb.types.User;
import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.properties.FacebookProperties;
import com.spring.websellspringmvc.services.authentication.AuthenticateServices;
import com.spring.websellspringmvc.services.authentication.FacebookLoginServices;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignInFaceBook", value = "/signInFacebook")
public class SignInFacebook extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            String facebookLoginUrl = FacebookProperties.getLoginURL();
            response.sendRedirect(facebookLoginUrl);
        } else {
            try {
                String accessToken = FacebookLoginServices.getINSTANCE().getToken(code);

                // Retrieve user's email address
                User userFacebook = (User) FacebookLoginServices.getINSTANCE().getUserInfo(accessToken);
                String emailFacebook = userFacebook.getEmail();

                com.spring.websellspringmvc.models.User userValid = AuthenticateServices.getINSTANCE().checkSignIn(emailFacebook);
                if (userValid != null) {
//                Tài khoản đã tồn tại
                    SessionManager.getInstance(request, response).addUser(userValid);
                    response.sendRedirect(ConfigPage.HOME);
                } else {
//                Tài khoản chưa tồn tại
                    request.setAttribute("email", emailFacebook);
                    RequestDispatcher dis = request.getRequestDispatcher(ConfigPage.SIGN_UP);
                    dis.forward(request, response);
                }
            } catch (FacebookException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve user data from Facebook.");
            }
        }
    }
}