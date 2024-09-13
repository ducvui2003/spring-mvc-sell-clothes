package com.spring.websellspringmvc.controller.api.user;

import com.spring.websellspringmvc.models.User;
import org.json.JSONObject;
import com.spring.websellspringmvc.services.UserServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.Encoding;
import com.spring.websellspringmvc.utils.ValidatePassword;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangePassword", value = "/api/user/password")
public class UpdatePasswordController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Not found");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject json = new JSONObject();

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        User user = SessionManager.getInstance(request, response).getUser();

        if (currentPassword == null || newPassword == null || confirmPassword == null) {
            json.put("error", "Missing required fields");
            json.put("isValid", false);
            response.getWriter().println(json.toString());
            return;
        }

        ValidatePassword validatePassword = new ValidatePassword(newPassword);
        boolean isValid = validatePassword.check();
        if (isValid) {
            UserServices.getINSTANCE().updateUserPassword(user.getId(), Encoding.getINSTANCE().toSHA1(newPassword));
            json.put("isValid", true);
        } else {
            json.put("isValid", false);
            json.put("error", validatePassword.getErrorMap());
        }
        response.getWriter().println(json.toString());
    }
}