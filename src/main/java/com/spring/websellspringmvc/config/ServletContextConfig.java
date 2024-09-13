package com.spring.websellspringmvc.config;

import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.ProductCardServices;
import com.spring.websellspringmvc.services.admin.AdminOrderServices;
import com.spring.websellspringmvc.utils.MoneyRange;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.ArrayList;
import java.util.List;

@WebListener
public class ServletContextConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        List<MoneyRange> moneyRangeList = new ArrayList<>();
//        Money Range
        moneyRangeList.add(new MoneyRange(0, 100000));
        moneyRangeList.add(new MoneyRange(100000, 300000));
        moneyRangeList.add(new MoneyRange(300000, 600000));
        context.setAttribute("moneyRangeList", moneyRangeList);
//Category
        List<Category> categoryList = ProductCardServices.getINSTANCE().getAllCategory();
        context.setAttribute("categoryList", categoryList);

        List<OrderStatus> listAllOrderStatus = AdminOrderServices.getINSTANCE().getListAllOrderStatus();
        context.setAttribute("listAllOrderStatus", listAllOrderStatus);

        List<TransactionStatus> listAllTransactionStatus = AdminOrderServices.getINSTANCE().getListAllTransactionStatus();
        context.setAttribute("listAllTransactionStatus", listAllTransactionStatus);

        List<DeliveryMethod> listAllDeliveryMethodManage = AdminOrderServices.getINSTANCE().getListAllDeliveryMethodManage();
        context.setAttribute("listAllDeliveryMethodManage", listAllDeliveryMethodManage);

        List<PaymentMethod> listAllPaymentMethodManage = AdminOrderServices.getINSTANCE().getListAllPaymentMethodManage();
        context.setAttribute("listAllPaymentMethodManage", listAllPaymentMethodManage);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

}
