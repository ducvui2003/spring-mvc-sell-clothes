package com.spring.websellspringmvc.controller.web.authentication;

import com.spring.websellspringmvc.config.ConfigPage;
import lombok.extern.slf4j.Slf4j;
import com.spring.websellspringmvc.services.authentication.AuthenticateServices;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@Slf4j
@WebServlet(name = "verify", value = "/verify")
public class Verify extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String tokenVerify = request.getParameter("token-verify");
        log.info("username {} tokenVerify {}", username, tokenVerify);
        boolean status = AuthenticateServices.getINSTANCE().verify(username, tokenVerify);

        request.setAttribute("username", username);
        if (status) {
            request.getRequestDispatcher(ConfigPage.VERIFY).forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}