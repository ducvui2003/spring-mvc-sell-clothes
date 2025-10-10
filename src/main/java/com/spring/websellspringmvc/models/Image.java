package com.spring.websellspringmvc.models;

import lombok.Data;
import lombok.Setter;

@Setter
@Data
public class Image {
  private int id;
  private String nameImage;
  private int productId;

  public Image() {}

  public Image(int id, String nameImage, int productId) {
    this.id = id;
    this.nameImage = nameImage;
    this.productId = productId;
  }
}
