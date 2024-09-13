package com.spring.websellspringmvc.controller.web.home;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.services.HomeServices;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "newProductsController", value = "/newProducts")
public class NewProductsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> listAllNewProducts = HomeServices.getINSTANCE().getListNewProducts(true);
        int page = 0, itemsPerPage = 8;
        int size = listAllNewProducts.size();
        int totalPage = (size % itemsPerPage == 0 ? (size / itemsPerPage) : ((size / itemsPerPage)) + 1);

        String xPage = request.getParameter("page");
        if (xPage == null) {
            page = 1;
        } else {
            try {
                page = Integer.parseInt(xPage);
            }catch (NumberFormatException exception){
                exception.printStackTrace();
            }
        }

        int start, end;
        start = (page - 1) * itemsPerPage;
        end = Math.min(page * itemsPerPage, size);
        List<Product> listProductsPerPage = getListProductsPerPage(listAllNewProducts, start, end);

        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("listProductsPerPage", listProductsPerPage);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(ConfigPage.PRODUCT_NEW);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public List<Product> getListProductsPerPage(List<Product> listProducts, int start, int end) {
        List<Product> listProductsPerPage = new ArrayList<>();
        for (int i = start; i < end; i++) {
            listProductsPerPage.add(listProducts.get(i));
        }
        return listProductsPerPage;
    }
}