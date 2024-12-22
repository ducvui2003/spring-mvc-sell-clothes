package com.spring.websellspringmvc.services.pdf;

import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface PDFService {
    byte[] createDataFile(OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse, String hash);

    File createFile( OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse, String hash);

    File createSignedFile(File orderFile, String signature) throws IOException;

    String readSignature(File signedFile);

    String readHash(File signedFile) throws IOException;

}
