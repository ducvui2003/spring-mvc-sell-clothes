package com.spring.websellspringmvc.utils;

import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.utils.constraint.PaymentMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class SignedOrderFile {

    public boolean verifyData(byte[] plainData, String sign, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
        signature.initVerify(publicKey);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(plainData));
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = bufferedInputStream.read(buffer)) >= 0) {
                signature.update(buffer, 0, len);
            }
            bufferedInputStream.close();
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (IOException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String  hashData(OrderDetailResponse detailResponse) throws NoSuchAlgorithmException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write((detailResponse.getOrderId() + "\n").getBytes());
//            bos.write((detailResponse.getStatus() + "\n").getBytes());
            bos.write((detailResponse.getFullName() + "\n").getBytes());
            bos.write((detailResponse.getPhone() + "\n").getBytes());
            bos.write((detailResponse.getEmail() + "\n").getBytes());
            bos.write((detailResponse.getProvince() + "\n").getBytes());
            bos.write((detailResponse.getDistrict() + "\n").getBytes());
            bos.write((detailResponse.getWard() + "\n").getBytes());
            bos.write((detailResponse.getDetail() + "\n").getBytes());
            bos.write((detailResponse.getPayment() + "\n").getBytes());
            //delimeter between order and items
            bos.write(("\n").getBytes());
            for (OrderDetailItemResponse item : detailResponse.getItems()) {
                bos.write((item.getName() + "\n").getBytes());
                bos.write((item.getSize() + "\n").getBytes());
                bos.write((item.getColor() + "\n").getBytes());
                bos.write((item.getQuantity() + "\n").getBytes());
                bos.write((item.getPrice() + "\n").getBytes());
                bos.write((item.getThumbnail() + "\n").getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bos.toByteArray());

        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    public  File createTempFile(MultipartFile multipartFile) {
        try {
            File tempFile = File.createTempFile("temp", multipartFile.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(multipartFile.getBytes());
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Error creating temporary file", e);
        }
    }
}

