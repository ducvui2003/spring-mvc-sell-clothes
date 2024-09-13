package com.spring.websellspringmvc.controller.api.admin.category;

import com.google.gson.JsonObject;
import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import com.spring.websellspringmvc.services.admin.AdminCategoryServices;
import com.spring.websellspringmvc.services.image.UploadImageServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "adminCreateCategory", value = "/api/admin/category/create")
@MultipartConfig(
        fileSizeThreshold = 1024 * 12024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 100
)
public class CreateCategoryController extends HttpServlet {
    private Category category;
    private List<Parameter> parameterList;
    private UploadImageServices uploadImageSizeGuideServices;
    private UploadImageServices uploadImageParameterGuideImgServices;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        try {
            String nameCategory = request.getParameter("nameCategory");
            String sizeTableImage = request.getParameter("sizeTableImage");
            String[] nameParameters = request.getParameterValues("nameParameter[]");
            String[] unit = request.getParameterValues("unit[]");
            String[] minValue = request.getParameterValues("minValue[]");
            String[] maxValue = request.getParameterValues("maxValue[]");
            String[] guideImg = request.getParameterValues("guideImg[]");

            if (nameParameters == null || unit == null || minValue == null || maxValue == null || guideImg == null) {
                throw new AppException(ErrorCode.MISSING_REQUEST);
            }

            if (nameParameters.length != unit.length || nameParameters.length != minValue.length || nameParameters.length != maxValue.length || nameParameters.length != guideImg.length) {
                throw new AppException(ErrorCode.MISSING_REQUEST);
            }

            this.category = new Category();
            category.setNameType(nameCategory);
            category.setSizeTableImage(sizeTableImage);

            this.parameterList = new ArrayList<>();
            for (int i = 0; i < nameParameters.length; i++) {
                Parameter parameter = new Parameter();
                parameter.setName(nameParameters[i]);
                try {
                    parameter.setMinValue(Double.parseDouble(minValue[i]));
                    parameter.setMaxValue(Double.parseDouble(maxValue[i]));
                    parameter.setUnit(unit[i]);
                    parameter.setGuideImg(guideImg[i]);
                } catch (NumberFormatException e) {
                    throw new AppException(ErrorCode.PARAMETER_ERROR);
                }
                parameterList.add(parameter);
            }
            int categoryId = AdminCategoryServices.getINSTANCE().addCategory(category);
            if (categoryId == -1)
                throw new AppException(ErrorCode.CATEGORY_ERROR);

            parameterList.forEach(parameter -> parameter.setCategoryId(categoryId));
            AdminCategoryServices.getINSTANCE().addParameters(parameterList);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Add category success");
            response.getWriter().println(jsonObject.toString());
        } catch (Exception e) {
            throw new AppException(ErrorCode.PARAMETER_ERROR);
        }
    }
}