package com.spring.websellspringmvc.dto.response;

import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailResponse {
  Category category;
  List<Parameter> parameters;
}
