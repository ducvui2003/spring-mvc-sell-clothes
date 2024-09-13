package com.spring.websellspringmvc.filter.shoppingCart;

import com.spring.websellspringmvc.models.shoppingCart.ShoppingCart;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.session.SessionManager;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter("/*")
public class VoucherValidityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getServletPath();

        HttpSession session = request.getSession(true);
        User user = SessionManager.getInstance(request, response).getUser();
        if (user != null){
            String userIdCart = String.valueOf(user.getId());
            ShoppingCart cart = (ShoppingCart) session.getAttribute(userIdCart);

            if (!requestURI.contains("shoppingCart.jsp") && !requestURI.contains("ShoppingCart") && !requestURI.contains("IncreaseQuantity")
                    && !requestURI.contains("DecreaseQuantity") && !requestURI.contains("DeleteCartProduct") && !requestURI.contains("ApplyVoucher") && !requestURI.contains("checkout.jsp") && !requestURI.contains("Checkout")) {
                session.removeAttribute("successApplied");
                session.removeAttribute("failedApply");
                session.removeAttribute("promotionCode");

//                if (cart != null && cart.getVoucherApplied() != null) {
//                    cart.setVoucherApplied(null);
//                    session.setAttribute(userIdCart, cart);
//                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
