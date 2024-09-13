package com.spring.websellspringmvc.controller.api.voucher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.voucher.VoucherServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(value = "/api/voucher/getAll")
public class LoadVoucherController extends HttpServlet {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd") // Customize date format here
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        try {
            String[] idsParam = req.getParameterValues("id[]");
            if (idsParam != null) {
                List<Integer> ids = List.of(idsParam).stream().map(Integer::parseInt).collect(Collectors.toList());
                List<Voucher> vouchers = VoucherServices.getINSTANCE().getAll(ids);
                JsonArray jsonArray = gson.toJsonTree(vouchers).getAsJsonArray();
                jsonObject.add("vouchers", jsonArray);
            }
            jsonObject.addProperty("success", true);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.addProperty("success", false);
        }
        resp.getWriter().write(gson.toJson(jsonObject));
    }
}
