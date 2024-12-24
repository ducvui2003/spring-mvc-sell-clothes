package com.spring.websellspringmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.websellspringmvc.models.Color;
import com.spring.websellspringmvc.models.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailItemChangedResponse extends OrderDetailItemResponse {
    List<Size> sizes;
    List<Color> colors;
}
