package com.spring.websellspringmvc.services.image;

import jakarta.servlet.http.Part;
import java.util.List;

//Interface dùng để quản lý các services upload ảnh
//Mở rộng các services upload ảnh khác nhau, nếu
public interface IUpload {
    String getImage(String folderPath, String imageName);

    List<String> getImages(String folderPath, List<String> imageNameArray);

    void uploadImage(String folderName, String imageName, Part part) throws Exception;

    void uploadImages(String folderName, String imageName, Part[] parts) throws Exception;

    void createFolder(String folderName) throws Exception;
}