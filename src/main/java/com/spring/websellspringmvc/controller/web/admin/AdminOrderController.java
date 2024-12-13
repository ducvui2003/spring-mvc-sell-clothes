package com.spring.websellspringmvc.controller.web.admin;

import com.spring.websellspringmvc.models.OrderStatus;
import com.spring.websellspringmvc.models.PaymentMethod;
import com.spring.websellspringmvc.models.TransactionStatus;
import com.spring.websellspringmvc.services.AdminOrderServices;
import com.spring.websellspringmvc.services.state.OrderState;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller("adminOrderControllerMVC")
@RequiredArgsConstructor
@RequestMapping("/admin/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {
    AdminOrderServices orderServices;

    @GetMapping
    public ModelAndView showOrderPage() {
        ModelAndView mov = new ModelAndView(PageAddress.ADMIN_ORDER.getPage());
        List<OrderStatus> orderStatusList = orderServices.getListAllOrderStatus();
        List<PaymentMethod> paymentMethodList = orderServices.getListAllPaymentMethodManage();
        List<TransactionStatus> transactionStatusList = orderServices.getListAllTransactionStatus();
        mov.addObject("orderStatus", orderStatusList);
        mov.addObject("paymentMethod", paymentMethodList);
        mov.addObject("transactionStatus", transactionStatusList);
        return mov;
    }
}
