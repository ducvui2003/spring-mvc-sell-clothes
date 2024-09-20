package com.spring.websellspringmvc.controller.api.checkout;

import com.spring.websellspringmvc.models.DeliveryInfo;
import com.spring.websellspringmvc.models.DeliveryInfoStorage;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.models.shoppingCart.ShoppingCart;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ChoiceDeliveryInfoController", value = "/api/checkout/delivery/choice")
public class ChoiceDeliveryInfoController extends HttpServlet {
    SessionManager sessionManager;
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String deliveryInfoKey = (String) request.getAttribute("deliveryInfoKey");
        HttpSession session = request.getSession(true);
        User user = sessionManager.getUser();
        String userIdCart = String.valueOf(user.getId());
        DeliveryInfoStorage deliveryInfoStorage = (DeliveryInfoStorage) session.getAttribute("deliveryInfoStorage");
        ShoppingCart cart = (ShoppingCart) session.getAttribute(userIdCart);
        session.setAttribute(userIdCart, cart);

        response.getWriter().write("Đã chọn");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}