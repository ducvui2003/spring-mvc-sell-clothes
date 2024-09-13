package com.spring.websellspringmvc.controller.api.admin.voucher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.websellspringmvc.models.Voucher;
import org.json.JSONObject;
import com.spring.websellspringmvc.services.voucher.VoucherServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminVoucher",value = "/api/admin/voucher/datatable")
public class VoucherDatatableController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer draw = Integer.parseInt(req.getParameter("draw"));
        Integer start = Integer.parseInt(req.getParameter("start"));
        Integer length = Integer.parseInt(req.getParameter("length"));
        String searchValue = req.getParameter("search[value]");
        String orderColumn = req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]");
        orderDir = orderDir == null ? "asc" : orderDir;

        // Mapping order column index to database column name
        String[] columnNames = {"code", "availableTurns", "createAt", "createAt", "state"};
        int ind = orderColumn == null ? 0 : Integer.parseInt(orderColumn);
        String orderBy = orderColumn == null ? "id" : (ind < columnNames.length ? columnNames[ind] : columnNames[0]);
        // Fetch filtered and sorted logs
        List<Voucher> vouchers = VoucherServices.getINSTANCE().getVoucher(start, length, searchValue, orderBy, orderDir);
        long size = VoucherServices.getINSTANCE().getTotalWithCondition(searchValue);

        // Prepare JSON response
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("draw", draw);
        jsonObject.put("recordsTotal", size);
        jsonObject.put("recordsFiltered", size);
        jsonObject.put("data", vouchers);
        // Prepare JSON response

        // Send response
        PrintWriter writer = resp.getWriter();
        writer.write(mapper.writeValueAsString(jsonObject.toMap()));
        writer.flush();
    }
}
