package com.spring.websellspringmvc.controller.api.admin.product;

import com.google.gson.JsonObject;
import com.spring.websellspringmvc.services.admin.AdminProductServices;
import com.spring.websellspringmvc.services.state.ProductState;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/api/admin/product/visible")
public class VisibleProductController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParameter = request.getParameter("id");
        String type = request.getParameter("type");
        JsonObject jsonObject = new JsonObject();
        int productId;
        if (idParameter == null || type == null) {
            jsonObject.addProperty("success", false);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(jsonObject.toString());
            return;
        }
        try {
            ProductState state = ProductState.valueOf(type.toUpperCase());
            productId = Integer.parseInt(idParameter);
            AdminProductServices.getINSTANCE().updateVisibility(productId, state);
            jsonObject.addProperty("success", true);
        } catch (NumberFormatException e) {
            jsonObject.addProperty("success", false);
        }
        response.getWriter().write(jsonObject.toString());
    }
}
