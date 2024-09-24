package com.spring.websellspringmvc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.websellspringmvc.services.state.ProductState;
import com.spring.websellspringmvc.utils.constraint.State;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChangeVisibilityProductRequest {
    @NotNull
    Integer id;
    @JsonProperty("type")
    @NotNull
    State state;
}
