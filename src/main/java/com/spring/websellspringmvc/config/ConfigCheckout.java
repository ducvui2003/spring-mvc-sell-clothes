package com.spring.websellspringmvc.config;

import com.spring.websellspringmvc.models.DeliveryMethod;
import com.spring.websellspringmvc.models.PaymentMethod;
import com.spring.websellspringmvc.services.checkout.CheckoutServicesImpl;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.util.List;

//@WebListener
public class ConfigCheckout implements ServletContextListener {
    CheckoutServicesImpl checkoutServicesImpl;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        List<DeliveryMethod> listDeliveryMethod = checkoutServicesImpl.getAllInformationDeliveryMethod();
        List<PaymentMethod> listPaymentMethod = checkoutServicesImpl.getAllPaymentMethod();
        context.setAttribute("listDeliveryMethod", listDeliveryMethod);
        context.setAttribute("listPaymentMethod", listPaymentMethod);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }


}