package com.spring.websellspringmvc.services.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import com.spring.websellspringmvc.models.Key;
import com.spring.websellspringmvc.utils.SignedOrderFile;
import com.spring.websellspringmvc.utils.constraint.OrderStatus;
import com.spring.websellspringmvc.utils.constraint.PaymentMethod;
import com.spring.websellspringmvc.utils.constraint.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
public class Test {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException {
        // Create mock OrderDetailResponse
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setOrderId("ORD123");
        orderDetailResponse.setFullName("John Doe");
        orderDetailResponse.setPhone("0123456789");
        orderDetailResponse.setEmail("john.doe@example.com");
        orderDetailResponse.setProvince("Hanoi");
        orderDetailResponse.setDistrict("Cau Giay");
        orderDetailResponse.setWard("My Dinh");
        orderDetailResponse.setDetail("123 My Street");
        orderDetailResponse.setPayment("Credit Card");
        orderDetailResponse.setDateOrder(LocalDateTime.now());
        orderDetailResponse.setFee(20000);
        OrderDetailItemResponse item1 = new OrderDetailItemResponse();
        item1.setId(1);
        item1.setName("Product 1");
        item1.setSize("M");
        item1.setColor("Red");
        item1.setQuantity(2);
        item1.setPrice(50000);
        item1.setThumbnail("https://example.com/product1.jpg");
        OrderDetailItemResponse item2 = new OrderDetailItemResponse();
        item2.setId(2);
        item2.setName("Product 2");
        item2.setSize("S");
        item2.setColor("Yellow");
        item2.setQuantity(3);
        item2.setPrice(100000);
        item2.setThumbnail("https://example.com/product2.jpg");
        List<OrderDetailItemResponse> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        orderDetailResponse.setItems(items);


        // Create mock AdminOrderDetailResponse
        AdminOrderDetailResponse adminOrderDetailResponse = new AdminOrderDetailResponse();
        adminOrderDetailResponse.setId("ORD123");
        adminOrderDetailResponse.setFullName("John Doe");
        adminOrderDetailResponse.setEmail("john.doe@example.com");
        adminOrderDetailResponse.setPhone("0123456789");
        adminOrderDetailResponse.setPaymentMethod(PaymentMethod.VNPAY); // mock PaymentMethod
        adminOrderDetailResponse.setTransactionStatus(TransactionStatus.UN_PAID); // mock TransactionStatus
        adminOrderDetailResponse.setOrderStatus(OrderStatus.PENDING); // mock OrderStatus
        adminOrderDetailResponse.setFee(20000);
        adminOrderDetailResponse.setItems(orderDetailResponse.getItems());
        adminOrderDetailResponse.setDateOrder(LocalDateTime.now());

        // Create an instance of PDFServiceImpl
        PDFServiceImpl pdfService = new PDFServiceImpl();

        File inputFile = new File("src/main/java/com/spring/websellspringmvc/services/pdf/Test.java");

        // Test createFile method (returns PDF file)
//        try {
//            File pdfFile = pdfService.createFile(inputFile, orderDetailResponse, Arrays.asList(adminOrderDetailResponse), "ma hash");
//            System.out.println("PDF File generated: " + pdfFile.getAbsolutePath());
//        } catch (Exception e) {
//            System.err.println("Error generating PDF file: " + e.getMessage());
//        }
//
//         Test createSignedFile method (sign PDF)
        try {
            File signedFile = pdfService.createSignedFile(new File("src/main/java/com/spring/websellspringmvc/services/pdf/Invoice_ORD123.pdf"), "John Doe");
            System.out.println("Signed PDF File generated: " + signedFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error adding signature: " + e.getMessage());
        }
////
        String hash = pdfService.readHash(new File("src/main/java/com/spring/websellspringmvc/services/pdf/Signed_Invoice_ORD123.pdf"));
        System.out.println("Hash: " + hash);
//
        String signatureKey = pdfService.readSignature(new File("D:\\signed_d2b63ea3-98c9-4d76-93d1-6730f415aac8.pdf"));
        System.out.println(signatureKey);
        signatureKey = "ZDJiNjNlYTMtOThjOS00ZDc2LTkzZDEtNjczMGY0MTVhYWM4Ck1pbmggVOG6pW4KMDM1MjAzMzE5OQp0YW5kYW5taW4xQGdtYWlsLmNvbQpIxrBuZyBZw6puCkh1eeG7h24gVsSDbiBMw6JtCljDoyBMxrDGoW5nIFTDoGkKYWRhc2RhCkNPRAoKw4FvIHPGoSBtaSBTcXVhcmUgQ3Jvc3MKMlhMCiMwMDAwRkYKMgo3MDQwMDAuMApodHRwczovL3Jlcy5jbG91ZGluYXJ5LmNvbS95b3Vyc3R5bGUvaW1hZ2UvdXBsb2FkL2Nfc2NhbGUvcV9hdXRvL2ZfYXV0by92MS9wcm9kdWN0X2ltZy81Ni9wcm9kdWN0NTYuanBnCg==";

        String signature = pdfService.readSignature(new File("src/main/java/com/spring/websellspringmvc/services/pdf/Signed_Invoice_ORD123.pdf"));
        System.out.println("signature: " + signature);

        // Ghi dữ liệu vào metadata của file signed_Invoice_ORD123.pdf
//        createPdf();
    }


//    public static void createPdf() throws IOException {
//        String inputHTML="D:\\university\\ATTT\\web-sell-spring-mvc\\src\\main\\resources\\templates\\templateEmailPlaceOrder.html";
//        String outputPdf="D:\\university\\ATTT\\web-sell-spring-mvc\\src\\main\\resources\\templates\\templateEmailPlaceOrder.pdf";
//        Document document = Jsoup.parse(new File(inputHTML), "UTF-8");
//        try (OutputStream os = new FileOutputStream(outputPdf)) {
//            PdfRendererBuilder builder = new PdfRendererBuilder();
//            builder.withUri(outputPdf);
//            builder.toStream(os);
//            System.out.println(document.html());
//            // If you need the file path, use getResource()
//            URL fontUrl = Test.class.getResource("/templates/roboto.ttf");
//            builder.useFont(new File(fontUrl.toURI()), "roboto");
//            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
//            builder.run();
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
