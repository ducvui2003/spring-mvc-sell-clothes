package com.spring.websellspringmvc.models;

import lombok.Data;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import lombok.Setter;

import java.util.Objects;

@Setter
@Data
public class Image {
    private int id;
    private String nameImage;
    private int productId;

    public Image() {
    }

    public Image(int id, String nameImage, int productId) {
        this.id = id;
        this.nameImage = nameImage;
        this.productId = productId;
    }
    
    public String getNameImage() {
        return CloudinaryUploadServices.getINSTANCE().getImage("product_img", this.nameImage);
    }

}
