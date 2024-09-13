package com.spring.websellspringmvc.controller.api.voucher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.voucher.VoucherServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/api/voucher/get")
public class GetVoucherController extends HttpServlet {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd") // Customize date format here
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Voucher> vouchers = VoucherServices.getINSTANCE().getAll();
        resp.getWriter().write(gson.toJson(vouchers));
    }
}
