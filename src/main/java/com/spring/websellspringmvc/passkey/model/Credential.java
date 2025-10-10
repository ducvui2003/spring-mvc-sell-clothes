package com.spring.websellspringmvc.passkey.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Credential {
  String id;
  String name;
  String userId;
  String publicKey;
  long signCount;
  String type;

  @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy ")
  LocalDateTime createdAt;
}
