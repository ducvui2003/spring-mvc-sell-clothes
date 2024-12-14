package com.spring.websellspringmvc.controller.web.admin;

import com.spring.websellspringmvc.models.OrderStatus;
import com.spring.websellspringmvc.models.TransactionStatus;
import com.spring.websellspringmvc.services.admin.AdminOrderServices;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import com.spring.websellspringmvc.utils.constraint.PaymentMethod;
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
        List<TransactionStatus> transactionStatusList = orderServices.getListAllTransactionStatus();
        mov.addObject("orderStatus", orderStatusList);
        mov.addObject("transactionStatus", transactionStatusList);
        mov.addObject("paymentMethod", PaymentMethod.values());
        return mov;
    }
}
