package com.spring.websellspringmvc.controller.web;

import com.spring.websellspringmvc.dto.mvc.request.CheckoutRequest;
import com.spring.websellspringmvc.dto.response.AddressResponse;
import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.services.address.AddressServices;
import com.spring.websellspringmvc.services.checkout.CheckoutServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.PageAddress;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CheckoutController {
    AddressServices addressService;
    CheckoutServices checkoutServices;
    SessionManager sessionManager;
    String VN_PAY_SUCCESS = "00";
    String VN_PAY_PROCESSING = "07";

    @PostMapping("/checkout")
    public ModelAndView checkout(@ModelAttribute("checkout") CheckoutRequest request) {
        int userId = sessionManager.getUser().getId();
        List<AddressResponse> addresses = addressService.getAddress(userId);
        List<CartItemResponse> cartItems = checkoutServices.getCarts(request.getCartItemId(), userId);
        ModelAndView mov = new ModelAndView();
        mov.setViewName(PageAddress.CHECKOUT.getPage());
        mov.addObject("cartItems", cartItems);
        mov.addObject("addresses", addresses);
        return mov;
    }


    @GetMapping("/checkout/vn-pay-return")
    public ModelAndView checkout(@RequestParam("vnp_ResponseCode") String responseCode,
                                 @RequestParam("vnp_TxnRef") String vnp_TxnRef) {
        log.info("vnp_ResponseCode: " + responseCode);
        log.info("vnp_TxnRef: " + vnp_TxnRef);
        String status = "";
        switch (responseCode) {
            case VN_PAY_SUCCESS:
                checkoutServices.updateTransactionStatusVNPay(vnp_TxnRef, TransactionStatus.PAID);
                status = "Giao dịch thành công";
                break;
            case VN_PAY_PROCESSING:
                checkoutServices.updateTransactionStatusVNPay(vnp_TxnRef, TransactionStatus.PROCESSING);
                status = "Giao dịch đang được Vn Pay xử lý";
                break;
            default:
                checkoutServices.updateTransactionStatusVNPay(vnp_TxnRef, TransactionStatus.ERROR);
                status = "Giao dịch thất bại, vui lòng liên hệ Vn Pay";
                break;
        }
        ModelAndView mov = new ModelAndView();
        mov.addObject("status", status);
        mov.addObject("orderId", vnp_TxnRef);
        mov.setViewName(PageAddress.USER_ORDER_SUCCESS.getPage());
        return mov;
    }
}
