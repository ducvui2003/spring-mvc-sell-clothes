package com.spring.websellspringmvc.controller.web.product;

import com.spring.websellspringmvc.config.ConfigPage;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Review;
import com.spring.websellspringmvc.services.ProductCardServices;

import com.spring.websellspringmvc.services.ProductServices;
import com.spring.websellspringmvc.services.ReviewServices;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "showProductDetail", value = "/showProductDetail")
public class ShowProductDetail extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String idParameter = request.getParameter("id");

        int id;
        try {
            id = Integer.parseInt(idParameter.trim());
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        Product product = ProductServices.getINSTANCE().getProductByProductId(id);
        if (product == null) {
            response.sendError(404);
        } else {
//            Product
            request.setAttribute("product", product);
            //Reviews
            List<Review> listReview = getListReview(id);
            request.setAttribute("listReview", listReview);
//            Related product
            List<Product> listProductRelated = getListProductRandom(product.getCategoryId(), 4);
            request.setAttribute("listProductRelated", listProductRelated);
            request.getRequestDispatcher(ConfigPage.PRODUCT_DETAIL).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public List<Product> getListProductRandom(int categoryId, int quantity) {
        return ProductCardServices.getINSTANCE().getProductByCategoryId(categoryId, quantity, false);
    }
    public List<Review> getListReview(int productId) {
        return ReviewServices.getINSTANCE().getListReview(productId);
    }
}