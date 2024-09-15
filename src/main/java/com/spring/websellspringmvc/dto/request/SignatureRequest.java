package com.spring.websellspringmvc.dto.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SignatureRequest {
    int numberOfFiles;
    String[] publicId;
    String[] folder;
}
