package com.spring.websellspringmvc.controller.api.admin.order;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.spring.websellspringmvc.models.Order;
import com.spring.websellspringmvc.models.OrderStatus;
import com.spring.websellspringmvc.models.TransactionStatus;
import com.spring.websellspringmvc.services.admin.AdminOrderServices;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShowDialogUpdate", value = "/api/admin/order/update-dialog")
public class GetStatusOrderController extends HttpServlet {
    Gson gson = new GsonBuilder().create();

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<OrderStatus> listAllOrderStatus = AdminOrderServices.getINSTANCE().getListAllOrderStatus();
        List<TransactionStatus> listAllTransactionStatus = AdminOrderServices.getINSTANCE().getListAllTransactionStatus();

        String orderId = (String) request.getAttribute("orderId");
        Order order = AdminOrderServices.getINSTANCE().getOrderById(orderId);

        OrderStatus orderStatusTarget = AdminOrderServices.getINSTANCE().getOrderStatusById(order.getOrderStatusId());
        TransactionStatus transactionStatusTarget = AdminOrderServices.getINSTANCE().getTransactionStatusById(order.getTransactionStatusId());

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("listAllOrderStatus", gson.toJsonTree(listAllOrderStatus));
        jsonObject.add("listAllTransactionStatus", gson.toJsonTree(listAllTransactionStatus));

        jsonObject.add("orderStatusTarget", gson.toJsonTree(orderStatusTarget));
        jsonObject.add("transactionStatusTarget", gson.toJsonTree(transactionStatusTarget));

        response.getWriter().print(jsonObject);
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