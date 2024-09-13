package com.spring.websellspringmvc.controller.api.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.models.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.spring.websellspringmvc.services.ProductServices;
import com.spring.websellspringmvc.utils.ProductFactory;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "adminReadProduct", value = "/api/admin/product/read")
public class GetProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParameter = request.getParameter("id");

        int id;
        try {
            id = Integer.parseInt(idParameter.trim());
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        Product product = ProductServices.getINSTANCE().getProductByProductId(id);
        if (product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        List<Size> sizeList = ProductFactory.getListSizesByProductId(id);
        List<Color> colorList = ProductFactory.getListColorsByProductId(id);
        List<Image> imageList = ProductFactory.getListImagesByProductId(id);

        JSONObject jsonProduct = new JSONObject(product);
        JSONArray jsonSizes = new JSONArray(sizeList);
        JSONArray jsonColors = new JSONArray(colorList);
        JSONArray jsonImages = new JSONArray(imageList);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("product", jsonProduct);
        jsonResponse.put("sizes", jsonSizes);
        jsonResponse.put("colors", jsonColors);
        jsonResponse.put("images", jsonImages);

        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}