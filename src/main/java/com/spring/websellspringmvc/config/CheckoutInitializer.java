package com.spring.websellspringmvc.config;

import com.spring.websellspringmvc.models.DeliveryMethod;
import com.spring.websellspringmvc.models.PaymentMethod;
import com.spring.websellspringmvc.services.checkout.CheckoutServicesImpl;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckoutInitializer {
    ServletContext servletContext;
    CheckoutServicesImpl checkoutServicesImpl;


    @PostConstruct
    public void init() {
        List<DeliveryMethod> listDeliveryMethod = checkoutServicesImpl.getAllInformationDeliveryMethod();
        List<PaymentMethod> listPaymentMethod = checkoutServicesImpl.getAllPaymentMethod();
        servletContext.setAttribute("listDeliveryMethod", listDeliveryMethod);
        servletContext.setAttribute("listPaymentMethod", listPaymentMethod);
    }
}
