package com.spring.websellspringmvc.controller.api.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.spring.websellspringmvc.models.Address;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.AddressServices;
import com.spring.websellspringmvc.services.UserServices;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@WebServlet(name = "UpdateAddress", value = "/api/user/address")
public class AddressController extends HttpServlet {
    Gson gson = new GsonBuilder().create();
    final String ADDRESS_ADD = "create";
    final String ADDRESS_DELETE = "delete";
    final String ADDRESS_UPDATE = "update";
    private final AddressServices addressServices = AddressServices.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = SessionManager.getInstance(request, response).getUser().getId();
        List<Address> addressList = addressServices.getAddress(userId);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", HttpServletResponse.SC_OK);
        response.setStatus(HttpServletResponse.SC_OK);
        JsonArray jsonArray = new JsonArray();
        if (addressList != null) {
            jsonArray = gson.toJsonTree(addressList).getAsJsonArray();
        }
        jsonObject.add("address", jsonArray);
        response.getWriter().write(gson.toJson(jsonObject));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String province = request.getParameter("provinceName");
        String district = request.getParameter("districtName");
        String ward = request.getParameter("wardName");
        String detail = request.getParameter("detail");
        String action = request.getParameter("action");
        JsonObject jsonObject = new JsonObject();
        if (province == null || district == null || ward == null || detail == null) {
            jsonObject.addProperty("status", false);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(gson.toJson(jsonObject));
            return;
        }

        try {
            User user = SessionManager.getInstance(request, response).getUser();
            Address address = new Address();
            address.setProvince(province);
            address.setDistrict(district);
            address.setWard(ward);
            address.setDetail(detail);
            address.setUserId(user.getId());
            switch (action) {
                case ADDRESS_ADD:
                    Integer idAdded = addressServices.insertAddress(address);
                    jsonObject.addProperty("status", true);
                    jsonObject.addProperty("id", idAdded);
                    break;
                case ADDRESS_UPDATE:
                    address.setId(Integer.parseInt(id));
                    addressServices.updateAddress(address);
                    jsonObject.addProperty("status", true);
                    break;
            }
        } catch (NumberFormatException e) {
            jsonObject.addProperty("status", false);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(gson.toJson(jsonObject));
            return;
        } catch (URISyntaxException e) {
            jsonObject.addProperty("status", false);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(gson.toJson(jsonObject));
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(gson.toJson(jsonObject));
    }
}