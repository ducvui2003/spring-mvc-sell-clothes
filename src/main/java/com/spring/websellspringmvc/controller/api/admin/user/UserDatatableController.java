package com.spring.websellspringmvc.controller.api.admin.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.websellspringmvc.models.User;
import org.json.JSONObject;
import com.spring.websellspringmvc.services.UserServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/api/admin/user/datatable")
public class UserDatatableController extends HttpServlet {

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
        String[] columnNames = {"id", "username", "email", "fullName", "gender"};
        int ind = orderColumn == null ? 0 : Integer.parseInt(orderColumn);
        String orderBy = orderColumn == null ? "id" : (ind < columnNames.length ? columnNames[ind] : columnNames[0]);
        // Fetch filtered and sorted logs
        List<User> users = UserServices.getINSTANCE().getUser(start, length, searchValue, orderBy, orderDir);
        long size = UserServices.getINSTANCE().getTotalWithCondition(searchValue);

        // Prepare JSON response
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("draw", draw);
        jsonObject.put("recordsTotal", size);
        jsonObject.put("recordsFiltered", size);
        jsonObject.put("data", users);

        // Send response
        resp.getWriter().println(jsonObject.toString());
    }
}
