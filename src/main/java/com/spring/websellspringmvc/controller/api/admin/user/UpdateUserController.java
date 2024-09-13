package com.spring.websellspringmvc.controller.api.admin.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.UserServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet(value = "/api/admin/user/update")
public class UpdateUserController extends HttpServlet {
    Gson gson = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdString = request.getParameter("id");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthDayString = request.getParameter("birthDay");
        String role = request.getParameter("role");
        JsonObject jsonObject = new JsonObject();
        try {
            int userId = Integer.parseInt(userIdString);
            Date birthDay = Date.valueOf(birthDayString);
            User user = User.builder()
                    .id(userId)
                    .fullName(fullName)
                    .phone(phone)
                    .birthDay(birthDay)
                    .gender(gender)
                    .role(role)
                    .build();
            UserServices.getINSTANCE().updateUser(user);
            jsonObject.addProperty("success", true);
        } catch (Exception e) {
            jsonObject.addProperty("success", false);
        }
        response.getWriter().println(gson.toJson(jsonObject));
    }
}
