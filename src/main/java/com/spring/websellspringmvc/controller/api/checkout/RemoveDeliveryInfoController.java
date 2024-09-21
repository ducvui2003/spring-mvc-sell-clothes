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

@WebServlet(name = "RemoveDeliveryInfoController", value = "/api/checkout/delivery/remove")
public class RemoveDeliveryInfoController extends HttpServlet {
    SessionManager sessionManager;

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String deliveryInfoKey = (String) request.getAttribute("deliveryInfoKey");
        String statusChoice = (String) request.getAttribute("statusChoice");
        HttpSession session = request.getSession(true);
        DeliveryInfoStorage deliveryInfoStorage = (DeliveryInfoStorage) session.getAttribute("deliveryInfoStorage");
        deliveryInfoStorage.remove(deliveryInfoKey);

        User user = sessionManager.getUser();
        String userIdCart = String.valueOf(user.getId());
        ShoppingCart cart = (ShoppingCart) session.getAttribute(userIdCart);
        if (statusChoice.equals("Đã chọn")) {
            DeliveryInfo deliveryInfoAuth = deliveryInfoStorage.getDeliveryInfoByKey("defaultDeliveryInfo");
//            cart.setDeliveryInfo(deliveryInfoAuth);
            session.setAttribute(userIdCart, cart);
        }

        session.setAttribute("deliveryInfoStorage", deliveryInfoStorage);
        response.getWriter().write("Xóa thành công");
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