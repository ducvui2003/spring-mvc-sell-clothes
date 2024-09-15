//package com.spring.websellspringmvc.config;
//
//import com.spring.websellspringmvc.models.*;
//import com.spring.websellspringmvc.services.ProductCardServices;
//import com.spring.websellspringmvc.services.admin.AdminOrderServices;
//import com.spring.websellspringmvc.utils.MoneyRange;
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletContextEvent;
//import jakarta.servlet.ServletContextListener;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
//public class ServletContextConfig implements ServletContextListener {
//    AdminOrderServices adminOrderServices;
//    ProductCardServices productCardServices;
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        ServletContext context = sce.getServletContext();
//        List<MoneyRange> moneyRangeList = new ArrayList<>();
////        Money Range
//        moneyRangeList.add(new MoneyRange(0, 100000));
//        moneyRangeList.add(new MoneyRange(100000, 300000));
//        moneyRangeList.add(new MoneyRange(300000, 600000));
//        context.setAttribute("moneyRangeList", moneyRangeList);
////Category
//        List<Category> categoryList = productCardServices.getAllCategory();
//        context.setAttribute("categoryList", categoryList);
//
//        List<OrderStatus> listAllOrderStatus = adminOrderServices.getListAllOrderStatus();
//        context.setAttribute("listAllOrderStatus", listAllOrderStatus);
//
//        List<TransactionStatus> listAllTransactionStatus = adminOrderServices.getListAllTransactionStatus();
//        context.setAttribute("listAllTransactionStatus", listAllTransactionStatus);
//
//        List<DeliveryMethod> listAllDeliveryMethodManage = adminOrderServices.getListAllDeliveryMethodManage();
//        context.setAttribute("listAllDeliveryMethodManage", listAllDeliveryMethodManage);
//
//        List<PaymentMethod> listAllPaymentMethodManage = adminOrderServices.getListAllPaymentMethodManage();
//        context.setAttribute("listAllPaymentMethodManage", listAllPaymentMethodManage);
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        ServletContextListener.super.contextDestroyed(sce);
//    }
//
//}
