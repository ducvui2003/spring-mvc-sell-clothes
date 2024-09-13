package com.spring.websellspringmvc.config;

import com.spring.websellspringmvc.models.DeliveryMethod;
import com.spring.websellspringmvc.models.PaymentMethod;
import com.spring.websellspringmvc.services.CheckoutServices;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class ConfigCheckout implements ServletContextListener {

    public ConfigCheckout() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        List<DeliveryMethod> listDeliveryMethod = CheckoutServices.getINSTANCE().getAllInformationDeliveryMethod();
        List<PaymentMethod> listPaymentMethod = CheckoutServices.getINSTANCE().getAllPaymentMethod();
        context.setAttribute("listDeliveryMethod", listDeliveryMethod);
        context.setAttribute("listPaymentMethod", listPaymentMethod);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }


}