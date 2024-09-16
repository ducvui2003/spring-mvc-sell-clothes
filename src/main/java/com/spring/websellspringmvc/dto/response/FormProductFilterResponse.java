package com.spring.websellspringmvc.dto.response;

import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.utils.MoneyRange;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FormProductFilterResponse {
    List<MoneyRange> moneys;
    List<Category> categories;
    List<Size> sizes;
    List<Color> colors;
}
