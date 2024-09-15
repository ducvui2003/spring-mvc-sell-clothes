//package com.spring.websellspringmvc.controller.api.admin.order;
//
//import com.google.gson.JsonObject;
//import com.spring.websellspringmvc.controller.exception.AppException;
//import com.spring.websellspringmvc.controller.exception.ErrorCode;
//import com.spring.websellspringmvc.services.admin.AdminOrderServices;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet(name = "UpdateStatusOrderTran", value = "/api/admin/order/update-status")
//public class UpdateStatusOrderController extends HttpServlet {
//
//    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        JsonObject jsonObject = new JsonObject();
//        String orderId = (String) request.getAttribute("orderId");
//        try {
//            Integer orderStatusId = null, transactionStatusId = null;
//            if (request.getAttribute("orderStatusId") != null) {
//                orderStatusId = Integer.parseInt((String) request.getAttribute("orderStatusId"));
//            }
//            if (request.getAttribute("transactionStatusId") != null) {
//                transactionStatusId = Integer.parseInt((String) request.getAttribute("transactionStatusId"));
//            }
//
//            boolean updateSuccess = AdminOrderServices.getINSTANCE().updateOrder(orderId, orderStatusId, transactionStatusId);
//
//            if (!updateSuccess) throw new AppException(ErrorCode.UPDATE_FAILED);
//            jsonObject.addProperty("code", ErrorCode.UPDATE_SUCCESS.getCode());
//            jsonObject.addProperty("message", ErrorCode.UPDATE_SUCCESS.getMessage());
//            response.getWriter().print(jsonObject);
//        } catch (Exception exception) {
//            throw new AppException(ErrorCode.ERROR_PARAM_REQUEST);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        processRequest(request, response);
//    }
//}