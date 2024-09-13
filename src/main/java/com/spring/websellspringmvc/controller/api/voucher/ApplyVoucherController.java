package com.spring.websellspringmvc.controller.api.voucher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.dao.VoucherDAO;
import com.spring.websellspringmvc.dto.VoucherDTO;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.services.voucher.VoucherServices;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(value = "/api/voucher/apply")
public class ApplyVoucherController extends HttpServlet {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd") // Customize date format here
            .create();
    VoucherDAO voucherDAO = new VoucherDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        try {
            String code = req.getParameter("code");
            String[] idsParam = req.getParameterValues("id[]");
            User user = SessionManager.getInstance(req, resp).getUser();
            if (idsParam != null) {
                List<Integer> ids = List.of(idsParam).stream().map(Integer::parseInt).collect(Collectors.toList());
                VoucherDTO voucherDTO = VoucherServices.getINSTANCE().canApply(user, code, ids);
                jsonObject.add("result", gson.toJsonTree(voucherDTO));
                jsonObject.addProperty("code", 200);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonObject.addProperty("code", 1005);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.addProperty("code", 1005);
        }
        resp.getWriter().write(gson.toJson(jsonObject));
    }
}
