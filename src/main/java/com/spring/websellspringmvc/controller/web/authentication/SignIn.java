package com.spring.websellspringmvc.controller.web.authentication;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.models.shoppingCart.ShoppingCart;
import com.spring.websellspringmvc.services.authentication.AuthenticateServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.Validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "signIn", value = "/signIn")
public class SignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        Map<String, Integer> managerIp = (Map<String, Integer>) request.getAttribute("managerIp");

        Validation validation = AuthenticateServices.getINSTANCE().checkSignIn(username, password);
        if (validation.getObjReturn() != null) {
//            Cookie ko co user, ko sessionId
            // Khi nào gg bắt được bot thì mới có quản lý ip
            if(managerIp != null)
                managerIp.put(request.getRemoteAddr(), 0);
            User userAuth = (User) validation.getObjReturn();
            SessionManager.getInstance(request, response).addUser(userAuth);
            request.getSession().setAttribute(userAuth.getId() + "", new ShoppingCart());
            response.sendRedirect(ConfigPage.HOME);
        } else {
            request.setAttribute("usernameError", validation.getFieldUsername());
            request.setAttribute("passwordError", validation.getFieldPassword());
            // Khi nào gg bắt được bot thì mới có quản lý ip
            if(managerIp != null)
                managerIp.put(request.getRemoteAddr(), managerIp.get(request.getRemoteAddr()) + 1);
            request.getRequestDispatcher(ConfigPage.SIGN_IN).forward(request, response);
        }
    }
}


