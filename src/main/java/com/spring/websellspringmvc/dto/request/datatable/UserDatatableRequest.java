package com.spring.websellspringmvc.dto.request.datatable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserDatatableRequest extends DatatableRequest{
    String name;
}
