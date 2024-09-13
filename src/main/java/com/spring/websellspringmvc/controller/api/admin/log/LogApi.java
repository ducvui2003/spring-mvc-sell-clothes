package com.spring.websellspringmvc.controller.api.admin.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.models.Log;
import org.json.JSONObject;
import com.spring.websellspringmvc.services.LogService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/admin/logAdmin")
public class LogApi extends HttpServlet {

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
        String[] columnNames = {"id", "ip", "level", "resource", "dateCreated"};
        int ind = orderColumn == null ? 0 : Integer.parseInt(orderColumn);
        String orderBy = orderColumn == null ? "id" : (ind < columnNames.length ? columnNames[ind] : columnNames[0]);
        // Fetch filtered and sorted logs
        List<Log> logs = LogService.getINSTANCE().getLog(start, length, searchValue, orderBy, orderDir);
        long size = LogService.getINSTANCE().getTotalWithCondition(searchValue);

        // Prepare JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("draw", draw);
        jsonObject.addProperty("recordsTotal", size);
        jsonObject.addProperty("recordsFiltered", size);
//        jsonObject.add("data", logs);
        // Send response
        PrintWriter writer = resp.getWriter();
//        writer.write(mapper.writeValueAsString(jsonObject.toMap()));
        writer.flush();
    }
}
