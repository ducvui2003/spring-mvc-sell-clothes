package com.spring.websellspringmvc.controller.api.shoppingCart;

import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.models.shoppingCart.ShoppingCart;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.models.shoppingCart.AbstractCartProduct;
import org.json.JSONObject;
import com.spring.websellspringmvc.services.ShoppingCartServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.FormatCurrency;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "DecreaseQuantityController", value = "/api/cart/decrease")
public class DecreaseQuantityController extends HttpServlet {
    SessionManager sessionManager;

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = 0;
        int cartProductIndex = 0;
        HttpSession session = request.getSession(true);
        User user = sessionManager.getUser();
        String userIdCart = String.valueOf(user.getId());
        ShoppingCart cart = (ShoppingCart) session.getAttribute(userIdCart);
        try {
            productId = Integer.parseInt((String) request.getAttribute("productId"));
            cartProductIndex = Integer.parseInt((String) request.getAttribute("cartProductIndex"));
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        cart.decrease(productId, cartProductIndex);
        String code = (String) session.getAttribute("promotionCode");
        JSONObject jsonObject = new JSONObject();
        response.setContentType("application/json");

//        if (code != null) {
//            Voucher voucher = cart.getVoucherApplied();
//            if (voucher == null) {
//                voucher = ShoppingCartServices.getINSTANCE().getValidVoucherApply(code);
//            }

//            if (cart.getTemporaryPrice() < voucher.getMinimumPrice()) {
//                double minPriceToApply = voucher.getMinimumPrice();
//                double currentTempPrice = cart.getTemporaryPrice();
//
//                double priceBuyMore = minPriceToApply - currentTempPrice;
//                String priceBuyMoreFormat = FormatCurrency.vietNamCurrency(priceBuyMore);
//                session.removeAttribute("successApplied");
//                cart.setVoucherApplied(null);
//                session.setAttribute("failedApply", "Bạn chưa đủ điều kiện để áp dụng mã " + code + ". Hãy mua thêm " + priceBuyMoreFormat);
//            }
//        }

        session.setAttribute(userIdCart, cart);

        AbstractCartProduct cartProduct = cart.getShoppingCartMap().get(productId).get(cartProductIndex);
        int newQuantity = cartProduct.getQuantity();
        String newSubtotalFormat = cartProduct.subtotalFormat();
        String newTemporaryPriceFormat = cart.temporaryPriceFormat();
//        String discountPriceFormat = cart.discountPriceFormat();
//        String newTotalPriceFormat = cart.totalPriceFormat(false);

        jsonObject.put("newQuantity", newQuantity);
        jsonObject.put("newSubtotalFormat", newSubtotalFormat);
        jsonObject.put("newTemporaryPriceFormat", newTemporaryPriceFormat);
//        jsonObject.put("newTotalPriceFormat", newTotalPriceFormat);
//        jsonObject.put("discountPrice", cart.getDiscountPrice());

        if (session.getAttribute("failedApply") != null) {
            jsonObject.put("failedApply", session.getAttribute("failedApply"));
        } else {
            jsonObject.remove("failedApply");
//            jsonObject.put("discountPriceFormat", discountPriceFormat);
        }
        response.getWriter().print(jsonObject);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}