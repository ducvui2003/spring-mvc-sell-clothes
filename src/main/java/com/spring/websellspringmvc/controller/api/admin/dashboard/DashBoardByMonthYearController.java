package com.spring.websellspringmvc.controller.api.admin.dashboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.dto.DashBoardDetail;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.services.admin.DashboardServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DashBoard", urlPatterns = {"/api/admin/dashboard/detail"})
public class DashBoardByMonthYearController extends HttpServlet {
    DashboardServices dashboardServices = DashboardServices.getINSTANCE();
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String month = req.getParameter("month");
        String year = req.getParameter("year");
        if (month.isBlank() || year.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        DashBoardDetail result = dashboardServices.getDashBoardDetail(month, year);
        JsonObject products = new JsonObject();
        JsonArray arrayProductsPopular = new JsonArray();
        JsonArray arrayProductsNotPopular = new JsonArray();
        for (Map.Entry<Product, Integer> entry : result.getProductPopular().entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("id", entry.getKey().getId());
            object.addProperty("name", entry.getKey().getName());
            object.addProperty("quantity", entry.getValue());
            arrayProductsPopular.add(object);
        }
        for (Map.Entry<Product, Integer> entry : result.getProductNotPopular().entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("id", entry.getKey().getId());
            object.addProperty("name", entry.getKey().getName());
            object.addProperty("quantity", entry.getValue());
            arrayProductsNotPopular.add(object);
        }
        products.add("notPopular", arrayProductsNotPopular);
        products.add("popular", arrayProductsPopular);
        products.addProperty("orderSuccess", result.getOrderSuccess());
        products.addProperty("orderFailed", result.getOrderFailed());
        products.addProperty("revenue", result.getRevenue());
        jsonObject.addProperty("code", 200);
        jsonObject.add("result", products);
        resp.getWriter().write(gson.toJson(jsonObject));
    }
}
