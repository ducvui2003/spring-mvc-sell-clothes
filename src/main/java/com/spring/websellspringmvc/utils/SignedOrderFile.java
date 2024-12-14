package com.spring.websellspringmvc.utils;

import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class SignedOrderFile {
    public byte[] writeDateFile(OrderDetailResponse orderDetailResponse) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write((orderDetailResponse.getOrderId() + "\n").getBytes());

            bos.write((orderDetailResponse.getStatus() + "\n").getBytes());
            bos.write((orderDetailResponse.getFullName() + "\n").getBytes());
            bos.write((orderDetailResponse.getPhone() + "\n").getBytes());
            bos.write((orderDetailResponse.getEmail() + "\n").getBytes());
            bos.write((orderDetailResponse.getProvince() + "\n").getBytes());
            bos.write((orderDetailResponse.getDistrict() + "\n").getBytes());
            bos.write((orderDetailResponse.getWard() + "\n").getBytes());
            bos.write((orderDetailResponse.getDetail() + "\n").getBytes());
            bos.write((orderDetailResponse.getPayment() + "\n").getBytes());
            bos.write((orderDetailResponse.getVoucherId() + "\n").getBytes());
            //delimeter between order and items
            bos.write(("\n").getBytes());
            for (int i = 0; i < orderDetailResponse.getItems().size(); i++) {
                OrderDetailItemResponse item = orderDetailResponse.getItems().get(i);
                bos.write((item.getName() + "\n").getBytes());
                bos.write((item.getSize() + "\n").getBytes());
                bos.write((item.getColor() + "\n").getBytes());
                bos.write((item.getQuantity() + "\n").getBytes());
                bos.write((Double.toString(item.getPrice()) + "\n").getBytes());
                bos.write((item.getThumbnail() + "\n").getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encode(bos.toByteArray());
    }

    public OrderDetailResponse readDataFile(byte[] fileData) {
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
            orderDetailResponse.setVoucherId(reader.readLine());

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

            return orderDetailResponse;
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Error while reading file data", e);
        }
    }
}
