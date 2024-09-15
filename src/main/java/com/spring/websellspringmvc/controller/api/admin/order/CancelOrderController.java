//package com.spring.websellspringmvc.controller.api.admin.order;
//
//import com.google.gson.JsonObject;
//import com.spring.websellspringmvc.models.Order;
//import com.spring.websellspringmvc.models.OrderStatus;
//
//import com.spring.websellspringmvc.services.admin.AdminOrderServices;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Random;
//
//@WebServlet(name = "CancelOrderAdmin", value = "/api/admin/order/cancel")
//public class CancelOrderController extends HttpServlet {
//
//    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String actionTarget = (String) request.getAttribute("action");
//        String[] multipleOrderId = (String[]) request.getAttribute("multipleOrderId");
//        AdminOrderServices.getINSTANCE().cancelOrderByMultipleId(multipleOrderId);
//
//        Random random = new Random();
//        String orderIdRepresent = multipleOrderId[random.nextInt(multipleOrderId.length)];
//        Order orderRepresent = AdminOrderServices.getINSTANCE().getOrderById(orderIdRepresent);
//        OrderStatus orderStatusRepresent = AdminOrderServices.getINSTANCE().getOrderStatusById(orderRepresent.getOrderStatusId());
//
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("successProcess", "Hủy đơn hàng có mã " + String.join(", ", multipleOrderId) + " thành công");
//        jsonObject.addProperty("cancelOrderAction", actionTarget);
//        jsonObject.addProperty("cancelStatus", orderStatusRepresent.getTypeStatus());
//        response.getWriter().print(jsonObject);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        processRequest(request, response);
//    }
//}