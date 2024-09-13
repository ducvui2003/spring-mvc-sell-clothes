package com.spring.websellspringmvc.controller.api.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.dao.IAddressDAO;
import com.spring.websellspringmvc.models.Address;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.AddressServices;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AddressDelete", value = "/api/user/address/delete")
public class AddressDeleteController extends HttpServlet {
    private final AddressServices addressServices = AddressServices.getINSTANCE();
    Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        String id = req.getParameter("id");
        if (id == null) {
            jsonObject.addProperty("status", false);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }

        try {
            Integer addressId = Integer.parseInt(id);
            SessionManager sessionManager = SessionManager.getInstance(req, resp);
            User user = sessionManager.getUser();
            if (user == null) {
                jsonObject.addProperty("status", false);
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }
            if (addressServices.deleteAddress(addressId, user.getId())) {
                jsonObject.addProperty("status", true);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }
            jsonObject.addProperty("status", false);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            jsonObject.addProperty("status", false);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }
}
