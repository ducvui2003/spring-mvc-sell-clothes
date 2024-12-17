package com.spring.websellspringmvc.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Key {
    String id;
    String publicKey;
    String previousId;
    int userId;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    boolean isDelete;
}
