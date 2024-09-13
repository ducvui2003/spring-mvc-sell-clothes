package com.spring.websellspringmvc.controller.api.admin.voucher;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.services.ProductServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "adminGetProduct", value = "/api/admin/voucher/get-product")
public class GetProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonArray jsonArray = new JsonArray();
        List<Product> listProduct = ProductServices.getINSTANCE().getAllProductSelect();
        for (Product product : listProduct) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", product.getId());
            jsonObject.addProperty("name", product.getName());
            jsonArray.add(jsonObject);
        }
        resp.getWriter().print(jsonArray);
    }
}
