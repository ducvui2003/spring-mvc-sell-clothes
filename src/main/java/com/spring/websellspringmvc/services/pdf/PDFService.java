package com.spring.websellspringmvc.services.pdf;

import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

public interface PDFService {
    byte[] createDataFile(OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse);

    File createFile(OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse);

    File createSignedFile(File orderFile, String signature);

    String readSignature(File signedFile);

}
