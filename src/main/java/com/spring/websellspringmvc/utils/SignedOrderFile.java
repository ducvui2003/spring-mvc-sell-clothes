package com.spring.websellspringmvc.utils;

import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.models.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class SignedOrderFile {
    public byte[] writeDateFile(OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write((detailResponse.getOrderId() + "\n").getBytes());
            bos.write((detailResponse.getStatus() + "\n").getBytes());
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
        for (AdminOrderDetailResponse order : orderDetailResponse) {
            try {
                bos.write((order.getId() + "\n").getBytes());
                bos.write((order.getFullName() + "\n").getBytes());
                bos.write((order.getPhone() + "\n").getBytes());
                bos.write((order.getEmail() + "\n").getBytes());
                bos.write((order.getProvince() + "\n").getBytes());
                bos.write((order.getDistrict() + "\n").getBytes());
                bos.write((order.getWard() + "\n").getBytes());
                bos.write((order.getDetail() + "\n").getBytes());
                bos.write((order.getPaymentMethod() + "\n").getBytes());
                bos.write(("\n").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Base64.getEncoder().encode(bos.toByteArray());
    }

    public Map<OrderDetailResponse, List<AdminOrderDetailResponse>> readDataFile(byte[] fileData) {
        try {
            // Decode Base64 encoded data back to bytes
            byte[] decodedData = Base64.getDecoder().decode(fileData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(decodedData)));

            // Read and populate OrderDetailResponse
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();

            // Read order-related fields
            orderDetailResponse.setOrderId(reader.readLine());
            orderDetailResponse.setStatus(reader.readLine());
            orderDetailResponse.setFullName(reader.readLine());
            orderDetailResponse.setPhone(reader.readLine());
            orderDetailResponse.setEmail(reader.readLine());
            orderDetailResponse.setProvince(reader.readLine());
            orderDetailResponse.setDistrict(reader.readLine());
            orderDetailResponse.setWard(reader.readLine());
            orderDetailResponse.setDetail(reader.readLine());
            orderDetailResponse.setPayment(reader.readLine());

            // Skip the delimiter line (empty line)
            reader.readLine();


            // Read items and populate the list
            List<OrderDetailItemResponse> items = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                OrderDetailItemResponse item = new OrderDetailItemResponse();
                item.setName(line);
                item.setSize(reader.readLine());
                item.setColor(reader.readLine());
                item.setQuantity(Integer.parseInt(reader.readLine()));
                item.setPrice(Double.parseDouble(reader.readLine()));
                item.setThumbnail(reader.readLine());
                items.add(item);
            }
            orderDetailResponse.setItems(items);
            List<AdminOrderDetailResponse> adminOrderDetailResponses = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                AdminOrderDetailResponse adminOrderDetailResponse = new AdminOrderDetailResponse();
                adminOrderDetailResponse.setId(line);
                adminOrderDetailResponse.setFullName(reader.readLine());
                adminOrderDetailResponse.setPhone(reader.readLine());
                adminOrderDetailResponse.setEmail(reader.readLine());
                adminOrderDetailResponse.setProvince(reader.readLine());
                adminOrderDetailResponse.setDistrict(reader.readLine());
                adminOrderDetailResponse.setWard(reader.readLine());
                adminOrderDetailResponse.setDetail(reader.readLine());
                adminOrderDetailResponse.setPaymentMethod(reader.readLine());
                adminOrderDetailResponses.add(adminOrderDetailResponse);
            }
            return Map.of(orderDetailResponse, adminOrderDetailResponses);
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Error while reading file data", e);
        }
    }

    public boolean verifyData(byte[] plaindata, String sign, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
        signature.initVerify(publicKey);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(plaindata));
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

    public String hashData(OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write((detailResponse.getOrderId() + "\n").getBytes());
            bos.write((detailResponse.getStatus() + "\n").getBytes());
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
        for (AdminOrderDetailResponse order : orderDetailResponse) {
            try {
                bos.write((order.getId() + "\n").getBytes());
                bos.write((order.getFullName() + "\n").getBytes());
                bos.write((order.getPhone() + "\n").getBytes());
                bos.write((order.getEmail() + "\n").getBytes());
                bos.write((order.getProvince() + "\n").getBytes());
                bos.write((order.getDistrict() + "\n").getBytes());
                bos.write((order.getWard() + "\n").getBytes());
                bos.write((order.getDetail() + "\n").getBytes());
                bos.write((order.getPaymentMethod() + "\n").getBytes());
                bos.write(("\n").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bos.toByteArray());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
