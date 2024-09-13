package com.spring.websellspringmvc.controller.api.admin.category;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.services.admin.AdminCategoryServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/admin/categories")
public class CategoryDatatableController extends HttpServlet {
    Gson gson = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> listCategory = AdminCategoryServices.getINSTANCE().getCategories();
        resp.getWriter().println(gson.toJsonTree(listCategory));
    }
}
