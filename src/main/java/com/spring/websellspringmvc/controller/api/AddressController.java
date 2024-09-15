package com.spring.websellspringmvc.controller.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.models.Address;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.AddressServices;
import com.spring.websellspringmvc.session.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    String ADDRESS_ADD = "create";
    String ADDRESS_DELETE = "delete";
    String ADDRESS_UPDATE = "update";
    AddressServices addressServices;
    Gson gson = new GsonBuilder().create();

    @GetMapping
    public void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = SessionManager.getInstance(request, response).getUser().getId();
        List<Address> addressList = addressServices.getAddress(userId);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", HttpServletResponse.SC_OK);
        response.setStatus(HttpServletResponse.SC_OK);
        JsonArray jsonArray = new JsonArray();
        if (addressList != null)
            jsonArray = gson.toJsonTree(addressList).getAsJsonArray();

        jsonObject.add("address", jsonArray);
        response.getWriter().write(gson.toJson(jsonObject));
    }

    @PostMapping
    public void updateAddress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    @PostMapping("/delete")
    public void deleteVoucher(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
