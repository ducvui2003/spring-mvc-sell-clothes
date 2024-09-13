package com.spring.websellspringmvc.controller.api.admin.voucher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.websellspringmvc.dto.VoucherDTO;
import com.spring.websellspringmvc.services.voucher.VoucherServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/api/admin/voucher/detail")
public class GetDetailVoucherController extends HttpServlet {
    VoucherServices voucherServices = VoucherServices.getINSTANCE();
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd") // Customize date format here
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (code == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        VoucherDTO voucherDTO = voucherServices.getDetail(code);
        resp.getWriter().println(gson.toJson(voucherDTO));
    }
}
