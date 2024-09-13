package com.spring.websellspringmvc.controller.api.admin.voucher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.voucher.VoucherServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "CreateVoucher", value = "/api/admin/voucher/create")
public class CreateVoucherController extends HttpServlet {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    VoucherServices voucherServices = VoucherServices.getINSTANCE();
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        try {
            Voucher voucher = Voucher.builder()
                    .createAt(new Date(System.currentTimeMillis()))
                    .code(req.getParameter("code"))
                    .availableTurns(Integer.parseInt(req.getParameter("availableTurns")))
                    .description(req.getParameter("description"))
                    .minimumPrice(Integer.parseInt(req.getParameter("minimumPrice")))
                    .discountPercent(Double.parseDouble(req.getParameter("discountPercent")))
                    .expiryDate(new Date(formatter.parse(req.getParameter("expiryDate")).getTime()))
                    .state(req.getParameter("state"))
                    .build();
            List<Integer> listProductId = Arrays.stream(req.getParameterValues("productId")).mapToInt(Integer::parseInt).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            boolean saveSuccess = voucherServices.saveVoucher(voucher,listProductId);
            jsonObject.addProperty("success", saveSuccess);
        } catch (Exception e) {
            jsonObject.addProperty("success", false);
        }
        resp.getWriter().print(gson.toJson(jsonObject));
    }
}
