package com.spring.websellspringmvc.controller.api.shoppingCart;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.services.ShoppingCartServices;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShoppingCartController", value = "/api/cart")
public class ShoppingCartController extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        RequestDispatcher requestDispatcher;
        if (action != null) {
            String productId = request.getParameter("productId");
            String cartProductIndex = request.getParameter("cartProductIndex");
            request.setAttribute("productId", productId);
            request.setAttribute("cartProductIndex", cartProductIndex);

            switch (action) {
                case "increaseQuantity" -> {
                    requestDispatcher = request.getRequestDispatcher("/api/cart/increase");
                    requestDispatcher.forward(request, response);
                }
                case "decreaseQuantity" -> {
                    requestDispatcher = request.getRequestDispatcher("/api/cart/decrease");
                    requestDispatcher.forward(request, response);
                }
                case "removeCartProduct" -> {
                    requestDispatcher = request.getRequestDispatcher("/api/cart/delete");
                    requestDispatcher.forward(request, response);
                }
                case "applyVoucher" -> {
                    String promotionCode = request.getParameter("promotionCode");
                    double temporaryPrice = Double.parseDouble(request.getParameter("temporaryPrice"));
                    request.setAttribute("promotionCode", promotionCode);
                    request.setAttribute("temporaryPrice", temporaryPrice);
                    requestDispatcher = request.getRequestDispatcher("/api/cart/apply-voucher");
                    requestDispatcher.forward(request, response);
                }
            }
        } else {
            request.getRequestDispatcher(ConfigPage.SIGN_IN).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Voucher> listVouchers = ShoppingCartServices.getINSTANCE().getListVouchers();
        request.setAttribute("listVouchers", listVouchers);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(ConfigPage.USER_CART);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}