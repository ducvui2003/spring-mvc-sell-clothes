package com.spring.websellspringmvc.services.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Services dùng để upload ảnh, service hiện tại đang sử dụng Cloudinary làm cloud lưu trữ ảnh
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CloudinaryUploadServices implements IUpload {
    @Value("${app.service.cloudinary.api-secret}")
    @NonFinal
    String apiSecret;
    Cloudinary cloudinary;
    // Chiều cao và chiều dài cho ảnh lấy (bên cloud tự điều chỉnh kích thước width và height, nếu để null thì lấy kích thước gốc của ảnh)
    //    Obj dùng để thao tác trên ảnh
    Transformation transformation;

    //    Lấy 1 link ảnh từ cloudinary
    @Override
    public String getImage(String folderPath, String imageName) {
        return cloudinary.url().transformation(transformation).generate(folderPath + "/" + imageName);
    }

    //    Lấy danh sách link ảnh từ cloudinary
    @Override
    public List<String> getImages(String folderPath, List<String> imageNameArray) {
        List<String> res = new ArrayList<>();

        for (String imageName : imageNameArray) {
            res.add(getImage(folderPath, imageName));
        }
        return res;
    }

    //    Upload 1 ảnh lên cloudinary
    @Override
    public void uploadImage(String folderName, String imageName, Part part) throws Exception {
        Map<String, Object> folderParams = ObjectUtils.asMap("folder", folderName);
        cloudinary.api().createFolder(folderName, folderParams);

        File tempFile = File.createTempFile("temp", null);
        part.write(tempFile.getAbsolutePath());
        cloudinary.uploader().uploadLarge(tempFile, ObjectUtils.asMap("folder", folderName, "public_id", imageName));
    }

    public void upload(String folderName, String imageName, String file) throws Exception {
        Map<String, Object> folderParams = ObjectUtils.asMap("folder", folderName);
        cloudinary.api().createFolder(folderName, folderParams);

        cloudinary.uploader().uploadLarge(file, ObjectUtils.asMap("folder", folderName, "public_id", imageName));
    }

    //    Upload nhiều ảnh lên cloudinary
    @Override
    public void uploadImages(String folderName, String imageName, Part[] parts) throws Exception {
        for (Part part : parts) {
            uploadImage(folderName, imageName, part);
        }
    }

    public void deleteImage(String imageFolder) throws IOException {
        cloudinary.uploader().destroy(imageFolder, ObjectUtils.emptyMap());
    }

    public void deleteImages(List<String> imagesFolder) throws IOException {
        for (String imageFolder : imagesFolder) {
            cloudinary.uploader().destroy(imageFolder, ObjectUtils.emptyMap());
        }
    }

    //    Tạo 1 folder trên cloudinary
    @Override
    public void createFolder(String folderName) throws Exception {
        Map<String, Object> folderParams = ObjectUtils.asMap("folder", folderName);
        cloudinary.api().createFolder(folderName, folderParams);
    }


    public String generateSignature(Map<String, Object> params) {
        return cloudinary.apiSignRequest(params, apiSecret);
    }
}