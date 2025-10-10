package com.spring.websellspringmvc.models;

import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Key {
  String id;
  String publicKey;
  int userId;
  LocalDateTime createAt;
  LocalDateTime updateAt;
  boolean deleted;
}
