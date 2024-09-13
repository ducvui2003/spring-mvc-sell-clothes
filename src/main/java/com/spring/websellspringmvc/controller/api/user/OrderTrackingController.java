package com.spring.websellspringmvc.controller.api.user;

import com.spring.websellspringmvc.dto.OrderResponseDTO;
import com.spring.websellspringmvc.models.User;
import org.json.JSONObject;
import com.spring.websellspringmvc.services.HistoryService;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderTracking", value = "/api/user/order")
public class OrderTrackingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int statusId = Integer.parseInt(request.getParameter("statusId"));
        User user = SessionManager.getInstance(request, response).getUser();
        List<OrderResponseDTO> orders = HistoryService.getINSTANCE().getOrder(user.getId(), statusId);
        JSONObject json = new JSONObject();
        json.put("data", orders);
        response.getWriter().print(json);
    }
}
