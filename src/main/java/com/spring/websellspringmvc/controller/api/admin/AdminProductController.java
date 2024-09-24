package com.spring.websellspringmvc.controller.api.admin;

import com.google.gson.JsonObject;
import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dto.request.ChangeVisibilityProductRequest;
import com.spring.websellspringmvc.dto.request.CreateProductRequest;
import com.spring.websellspringmvc.dto.request.DatatableRequest;
import com.spring.websellspringmvc.dto.request.UpdateProductRequest;
import com.spring.websellspringmvc.dto.response.DatatableResponse;
import com.spring.websellspringmvc.dto.response.ProductDetailAdminResponse;
import com.spring.websellspringmvc.dto.response.datatable.ProductDatatable;
import com.spring.websellspringmvc.mapper.ProductMapper;
import com.spring.websellspringmvc.models.Color;
import com.spring.websellspringmvc.models.Image;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Size;
import com.spring.websellspringmvc.services.ProductService;
import com.spring.websellspringmvc.services.admin.AdminProductServices;
import com.spring.websellspringmvc.utils.ProductFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController("apiAdminProductController")
@RequiredArgsConstructor
@RequestMapping("/api/admin/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AdminProductController {
    AdminProductServices adminProductServices;
    ProductService productService;
    ProductMapper productMapper = ProductMapper.INSTANCE;


    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        try {
            Product product = productMapper.toProduct(createProductRequest);
            product.setVisibility(true);
            product.setCreateAt(Date.valueOf(LocalDate.now()));

//        Add Product
            int productId = adminProductServices.addProduct(product);

            JsonObject objJson = new JsonObject();
//       Sản phẩm đã tồn tại
            if (productId == 0) throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

//        Add Size
            double[] sizePricesDouble = new double[createProductRequest.getSizePrice().length];
            for (int i = 0; i < sizePricesDouble.length; i++) {
                sizePricesDouble[i] = createProductRequest.getSizePrice()[i];
            }
            adminProductServices.addSize(createProductRequest.getNameSize(), sizePricesDouble, productId);

//        Add Color
            adminProductServices.addColor(createProductRequest.getColor(), productId);

//        Add Images
            List<Image> imagesAdded = Arrays.stream(createProductRequest.getNameImageAdded()).map(nameImage -> {
                Image image = new Image();
                image.setNameImage(nameImage);
                image.setProductId(productId);
                return image;
            }).collect(Collectors.toList());

            adminProductServices.addImages(imagesAdded);

            objJson.addProperty("code", ErrorCode.CREATE_SUCCESS.getCode());
            objJson.addProperty("message", ErrorCode.CREATE_SUCCESS.getMessage());
            return ResponseEntity.ok(objJson.toString());
        } catch (AppException e) {
            throw new AppException(ErrorCode.UPDATE_FAILED);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProductDetailAdminResponse> getProductDetail(@PathVariable("id") Integer id) {
        if (id == null)
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

        ProductDetailAdminResponse product = productService.getProductDetailAdmin(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping(value = "/visible")
    public ResponseEntity<String> visibleProduct(@RequestBody ChangeVisibilityProductRequest request) {
        JsonObject jsonObject = new JsonObject();
        try {
            productService.changeVisibility(request.getId(), request.getState().isValue());
            jsonObject.addProperty("success", true);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (NumberFormatException e) {
            jsonObject.addProperty("success", false);
            return ResponseEntity.badRequest().body(jsonObject.toString());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductRequest updateProductRequest) {
        if (updateProductRequest.getSalePrice() > updateProductRequest.getOriginalPrice())
            throw new AppException(ErrorCode.PRICE_ERROR);

        List<Image> imagesAdded = Arrays.stream(updateProductRequest.getNameSize()).map(nameImage -> {
            Image image = new Image();
            image.setNameImage(nameImage);
            image.setProductId(updateProductRequest.getId());
            return image;
        }).collect(Collectors.toList());


//        Update Product
        Product product = productMapper.toProduct(updateProductRequest);
        adminProductServices.updateProduct(product);

//        Update size
        Size[] sizes = new Size[updateProductRequest.getNameSize().length];
        for (int i = 0; i < updateProductRequest.getNameSize().length; i++) {
            Size size = new Size();
            size.setId(updateProductRequest.getSizeId()[i]);
            size.setNameSize(updateProductRequest.getNameSize()[i]);
            size.setSizePrice(updateProductRequest.getSizePrice()[i]);
            size.setProductId(updateProductRequest.getId());
            sizes[i] = size;
        }

        Color[] colors = new Color[updateProductRequest.getColor().length];
        for (int i = 0; i < colors.length; i++) {
            Color color = new Color();
            color.setId(Integer.parseInt(updateProductRequest.getColor()[i]));
            color.setCodeColor(updateProductRequest.getColor()[i]);
            color.setProductId(updateProductRequest.getId());
            colors[i] = color;
        }

//        Update sizes
        adminProductServices.updateSizes(sizes, updateProductRequest.getIdCategory());

//        Update colors
        adminProductServices.updateColors(colors, updateProductRequest.getId());

//        delete images
        if (updateProductRequest.getIdImageDeleted().length != 0)
            adminProductServices.deleteImages(Arrays.stream(updateProductRequest.getIdImageDeleted()).toList());

//        add images
        if (updateProductRequest.getNameImageAdded().length != 0)
            adminProductServices.addImages(imagesAdded);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", ErrorCode.UPDATE_SUCCESS.getCode());
        jsonObject.addProperty("message", ErrorCode.UPDATE_SUCCESS.getMessage());
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/datatable")
    public ResponseEntity<DatatableResponse<ProductDatatable>> getDatatable(@RequestBody DatatableRequest request) {
        DatatableResponse<ProductDatatable> response = productService.datatable(request);
        return ResponseEntity.ok(response);
    }
}
