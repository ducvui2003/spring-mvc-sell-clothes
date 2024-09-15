//package com.spring.websellspringmvc.controller.api.admin.order;
//
//import com.google.gson.JsonObject;
//import com.spring.websellspringmvc.services.admin.AdminOrderServices;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//import java.io.IOException;
//
//@WebServlet(name = "RemoveOrderAdmin", value = "/api/admin/order/remove")
//public class DeleteOrderController extends HttpServlet {
//
//    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String actionTarget = (String) request.getAttribute("action");
//        String[] multipleOrderId = (String[]) request.getAttribute("multipleOrderId");
//
//        AdminOrderServices.getINSTANCE().removeOrderByMultipleOrderId(multipleOrderId);
//
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("successProcess", "Xóa đơn hàng có mã " + String.join(",", multipleOrderId) +  " thành công");
//        jsonObject.addProperty("removeOrderAction", actionTarget);
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