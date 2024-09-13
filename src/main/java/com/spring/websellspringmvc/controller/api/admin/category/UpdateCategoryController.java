package com.spring.websellspringmvc.controller.api.admin.category;

import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import com.spring.websellspringmvc.properties.PathProperties;
import com.spring.websellspringmvc.services.admin.AdminCategoryServices;
import com.spring.websellspringmvc.services.image.UploadImageServices;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "adminUpdateCategory", value = "/api/admin/category/update")
@MultipartConfig(
        fileSizeThreshold = 1024 * 12024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 100
)
public class UpdateCategoryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder objJson = new StringBuilder();
        String idString = request.getParameter("id");
        String nameCategory = request.getParameter("nameCategory");
        Part sizeTableImagePart = request.getPart("sizeTableImage");
        int categoryId;
        try {
            categoryId = Integer.parseInt(idString);
            //        Category

            String sizeTableImage = uploadImageSizeTable(sizeTableImagePart);
            Category category = new Category();
            category.setId(categoryId);
            category.setNameType(nameCategory);
            category.setSizeTableImage(sizeTableImage);
//            AdminCategoryServices.getINSTANCE().updateCategory(category);

//        Parameters
            String[] nameParameters = request.getParameterValues("nameParameter");
            String[] minValues = request.getParameterValues("minValue");
            String[] maxValues = request.getParameterValues("maxValue");
            String[] units = request.getParameterValues("unit");

            Collection<Part> collectionGuideImage = new ArrayList<>();
            for (Part part : request.getParts()) {
                if (part.getName().equals("guideImg"))
                    collectionGuideImage.add(part);
            }
            List<String> guideImages = uploadGuideImages(collectionGuideImage);
            List<Parameter> listParameter = new ArrayList<>();
            for (int i = 0; i < nameParameters.length; i++) {
                Parameter parameter = new Parameter();
                parameter.setName(nameParameters[i]);
                parameter.setMinValue(Double.parseDouble(minValues[i]));
                parameter.setMaxValue(Double.parseDouble(maxValues[i]));
                parameter.setUnit(units[i]);
                parameter.setCategoryId(categoryId);
                parameter.setGuideImg(guideImages.get(i));
                listParameter.add(parameter);
            }
            AdminCategoryServices.getINSTANCE().updateParameters(listParameter, categoryId);
            objJson.append("{\"status\":").append("true}");
        } catch (NumberFormatException e) {
            objJson.append("{\"status\":").append("false}");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write(objJson.toString());
    }

    //Update image size table;
    private String uploadImageSizeTable(Part part) throws Exception {
        ServletContext servletContext = getServletContext();
        String root = servletContext.getRealPath("/") + PathProperties.getINSTANCE().getPathCategoryWeb();
        UploadImageServices uploadImageServices = new UploadImageServices(root);
        uploadImageServices.addImage(part);
        return uploadImageServices.getNameImages().get(0);
    }

    private List<String> uploadGuideImages(Collection<Part> parts) throws Exception {
        ServletContext servletContext = getServletContext();
        String root = servletContext.getRealPath("/") + PathProperties.getINSTANCE().getPathParameterWeb();
        UploadImageServices uploadImageServices = new UploadImageServices(root);
        uploadImageServices.addImages(parts);
        return uploadImageServices.getNameImages();
    }
}