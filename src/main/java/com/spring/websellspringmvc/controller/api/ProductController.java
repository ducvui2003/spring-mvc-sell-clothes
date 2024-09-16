package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.dto.ApiPaging;
import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.dto.response.FormProductFilterResponse;
import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.ProductCardServices;
import com.spring.websellspringmvc.services.ProductServices;
import com.spring.websellspringmvc.utils.MoneyRange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("apiProductController")
@RequiredArgsConstructor
@RequestMapping("/api/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductCardServices productCardServices;

    @GetMapping("/form-filter")
    public ResponseEntity<FormProductFilterResponse> formProduct() {
        List<MoneyRange> moneyRangeList = new ArrayList<>();
        moneyRangeList.add(new MoneyRange(0, 100000));
        moneyRangeList.add(new MoneyRange(100000, 300000));
        moneyRangeList.add(new MoneyRange(300000, 600000));

        List<Category> categoryList = productCardServices.getAllCategory();
        List<Size> sizeList = productCardServices.getAllSize();
        List<Color> colorList = productCardServices.getAllColor();

        return ResponseEntity.ok(FormProductFilterResponse.builder()
                .moneys(moneyRangeList)
                .sizes(sizeList)
                .colors(colorList)
                .categories(categoryList)
                .build());
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiPaging<ProductCardResponse>> filterProduct(
            @RequestParam(value = "category", required = false) List<Integer> categoryId,
            @RequestParam(value = "size", required = false) List<Integer> sizeId,
            @RequestParam(value = "color", required = false) List<Integer> colorId,
            @RequestParam(value = "money-from", required = false) List<String> moneyRange,
            @PageableDefault(page = 0, size = 9) Pageable page
    ) {
        ProductFilter productFilter = ProductFilter.of(page, categoryId, sizeId, colorId, moneyRange);
        Page<ProductCardResponse> productCardResponseList = productCardServices.filter(productFilter);

        return ResponseEntity.ok(ApiPaging.<ProductCardResponse>builder()
                .content(productCardResponseList.getContent())
                .page(productCardResponseList.getNumber())
                .size(productCardResponseList.getSize())
                .totalElement(productCardResponseList.getTotalElements())
                .totalPage(productCardResponseList.getTotalPages())
                .build());
    }
}
