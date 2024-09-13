package com.spring.websellspringmvc.controller.api.admin.category;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import com.spring.websellspringmvc.services.admin.AdminCategoryServices;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "adminReadCategory", value = "/api/admin/category/get")
public class GetCategoryController extends HttpServlet {
    Gson gson = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParameter = request.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idParameter);
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.CATEGORY_ERROR);
        }
        try {
            Category category = AdminCategoryServices.getINSTANCE().getCategoryById(id);
            List<Parameter> listParameter = AdminCategoryServices.getINSTANCE().getParametersByCategoryId(id);
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("category", gson.toJsonTree(category));
            jsonObject.add("parameters", gson.toJsonTree(listParameter));
            response.getWriter().println(jsonObject);
        } catch (IndexOutOfBoundsException e) {
            throw new AppException(ErrorCode.CATEGORY_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}