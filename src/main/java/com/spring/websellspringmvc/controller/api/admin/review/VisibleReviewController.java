package com.spring.websellspringmvc.controller.api.admin.review;

import com.google.gson.JsonObject;
import com.spring.websellspringmvc.services.admin.AdminProductServices;
import com.spring.websellspringmvc.services.admin.AdminReviewServices;
import com.spring.websellspringmvc.services.state.ProductState;
import com.spring.websellspringmvc.services.state.ReviewState;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/api/admin/review/visible")
public class VisibleReviewController extends HttpServlet {
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
            ReviewState state = ReviewState.valueOf(type.toUpperCase());
            productId = Integer.parseInt(idParameter);
            AdminReviewServices.getINSTANCE().updateVisibility(productId, state);
            jsonObject.addProperty("success", true);
        } catch (NumberFormatException e) {
            jsonObject.addProperty("success", false);
        }
        response.getWriter().write(jsonObject.toString());
    }
}
