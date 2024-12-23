package com.spring.websellspringmvc.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
