package com.spring.websellspringmvc.controller.api.admin;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.request.CreateCategoryRequest;
import com.spring.websellspringmvc.dto.response.CategoryDetailResponse;
import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import com.spring.websellspringmvc.services.admin.AdminCategoryServices;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/category")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryController {
    AdminCategoryServices adminCategoryServices;

    @GetMapping("/datatable")
    public ResponseEntity<List<Category>> getDatatable() {
        List<Category> listCategory = adminCategoryServices.getCategories();
        return ResponseEntity.ok(listCategory);
    }

    @GetMapping("/get")
    public ResponseEntity<CategoryDetailResponse> getDetail(@RequestParam("id") Integer id) {
        if (id == null)
            throw new AppException(ErrorCode.CATEGORY_ERROR);
        Category category = adminCategoryServices.getCategoryById(id);
        List<Parameter> parameters = adminCategoryServices.getParametersByCategoryId(id);
        return ResponseEntity.ok(CategoryDetailResponse.builder().category(category).parameters(parameters).build());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody CreateCategoryRequest request) {
        if (request.getNameParameter().length != request.getUnit().length || request.getNameParameter().length != request.getMinValue().length || request.getNameParameter().length != request.getMaxValue().length || request.getNameParameter().length != request.getGuideImg().length)
            throw new AppException(ErrorCode.MISSING_REQUEST);
        try {
            Category category = new Category();
            category.setNameType(category.getNameType());
            category.setSizeTableImage(category.getSizeTableImage());

            List<Parameter> parameterList = new ArrayList<>();
            for (int i = 0; i < request.getNameParameter().length; i++) {
                Parameter parameter = new Parameter();
                parameter.setName(request.getNameParameter()[i]);
                try {
                    parameter.setMinValue(Double.parseDouble(request.getMinValue()[i]));
                    parameter.setMaxValue(Double.parseDouble(request.getMaxValue()[i]));
                    parameter.setUnit(request.getUnit()[i]);
                    parameter.setGuideImg(request.getGuideImg()[i]);
                } catch (NumberFormatException e) {
                    throw new AppException(ErrorCode.PARAMETER_ERROR);
                }
                parameterList.add(parameter);
            }
            int categoryId = adminCategoryServices.addCategory(category);
            if (categoryId == -1)
                throw new AppException(ErrorCode.CATEGORY_ERROR);

            parameterList.forEach(parameter -> parameter.setCategoryId(categoryId));
            adminCategoryServices.addParameters(parameterList);
            return ResponseEntity.ok().body(ApiResponse.builder().code(200).message("Add category success").build());
        } catch (Exception e) {
            throw new AppException(ErrorCode.PARAMETER_ERROR);
        }
    }

//    @PutMapping("/update")
//    public ResponseEntity<ApiResponse<?>> updateCategory(@RequestBody CreateCategoryRequest request) {
//        StringBuilder objJson = new StringBuilder();
//        String idString = request.getParameter("id");
//        String nameCategory = request.getParameter("nameCategory");
//        Part sizeTableImagePart = request.getPart("sizeTableImage");
//        int categoryId;
//        try {
//            categoryId = Integer.parseInt(idString);
//            //        Category
//
//            String sizeTableImage = uploadImageSizeTable(sizeTableImagePart);
//            Category category = new Category();
//            category.setId(categoryId);
//            category.setNameType(nameCategory);
//            category.setSizeTableImage(sizeTableImage);
////            AdminCategoryServices.getINSTANCE().updateCategory(category);
//
////        Parameters
//            String[] nameParameters = request.getParameterValues("nameParameter");
//            String[] minValues = request.getParameterValues("minValue");
//            String[] maxValues = request.getParameterValues("maxValue");
//            String[] units = request.getParameterValues("unit");
//
//            Collection<Part> collectionGuideImage = new ArrayList<>();
//            for (Part part : request.getParts()) {
//                if (part.getName().equals("guideImg"))
//                    collectionGuideImage.add(part);
//            }
//            List<String> guideImages = uploadGuideImages(collectionGuideImage);
//            List<Parameter> listParameter = new ArrayList<>();
//            for (int i = 0; i < nameParameters.length; i++) {
//                Parameter parameter = new Parameter();
//                parameter.setName(nameParameters[i]);
//                parameter.setMinValue(Double.parseDouble(minValues[i]));
//                parameter.setMaxValue(Double.parseDouble(maxValues[i]));
//                parameter.setUnit(units[i]);
//                parameter.setCategoryId(categoryId);
//                parameter.setGuideImg(guideImages.get(i));
//                listParameter.add(parameter);
//            }
//            AdminCategoryServices.getINSTANCE().updateParameters(listParameter, categoryId);
//            objJson.append("{\"status\":").append("true}");
//        } catch (NumberFormatException e) {
//            objJson.append("{\"status\":").append("false}");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        response.getWriter().write(objJson.toString());
//    }
//
//    //Update image size table;
//    private String uploadImageSizeTable(Part part) throws Exception {
//        ServletContext servletContext = getServletContext();
//        String root = servletContext.getRealPath("/") + PathProperties.getINSTANCE().getPathCategoryWeb();
//        UploadImageServices uploadImageServices = new UploadImageServices(root);
//        uploadImageServices.addImage(part);
//        return uploadImageServices.getNameImages().get(0);
//    }
//
//    private List<String> uploadGuideImages(Collection<Part> parts) throws Exception {
//        ServletContext servletContext = getServletContext();
//        String root = servletContext.getRealPath("/") + PathProperties.getINSTANCE().getPathParameterWeb();
//        UploadImageServices uploadImageServices = new UploadImageServices(root);
//        uploadImageServices.addImages(parts);
//        return uploadImageServices.getNameImages();
//    }
}
