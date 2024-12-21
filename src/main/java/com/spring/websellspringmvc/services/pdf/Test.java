package com.spring.websellspringmvc.services.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
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
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
public class Test {
    public static void main(String[] args) throws IOException {
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
        orderDetailResponse.setFee(100.50);
        orderDetailResponse.setItems(Arrays.asList(
                new OrderDetailItemResponse(1, "Item 1", "S", "Red", 2, 20.00, "item1.jpg"),
                new OrderDetailItemResponse(2, "Item 2", "M", "Blue", 1, 30.00, "item2.jpg")
        ));

        // Create mock AdminOrderDetailResponse
        AdminOrderDetailResponse adminOrderDetailResponse = new AdminOrderDetailResponse();
        adminOrderDetailResponse.setId("ORD123");
        adminOrderDetailResponse.setFullName("John Doe");
        adminOrderDetailResponse.setEmail("john.doe@example.com");
        adminOrderDetailResponse.setPhone("0123456789");
        adminOrderDetailResponse.setPaymentMethod(PaymentMethod.VNPAY); // mock PaymentMethod
        adminOrderDetailResponse.setTransactionStatus(TransactionStatus.UN_PAID); // mock TransactionStatus
        adminOrderDetailResponse.setOrderStatus(OrderStatus.PENDING); // mock OrderStatus
        adminOrderDetailResponse.setFee(100.50);
        adminOrderDetailResponse.setItems(orderDetailResponse.getItems());
        adminOrderDetailResponse.setDateOrder(LocalDateTime.now());

        // Create an instance of PDFServiceImpl
        PDFServiceImpl pdfService = new PDFServiceImpl();

        File inputFile = new File("src/main/java/com/spring/websellspringmvc/services/pdf/Test.java");



        // Test createFile method (returns PDF file)
        try {
            File pdfFile = pdfService.createFile(inputFile, orderDetailResponse, Arrays.asList(adminOrderDetailResponse), "ma hash");
            System.out.println("PDF File generated: " + pdfFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error generating PDF file: " + e.getMessage());
        }

//         Test createSignedFile method (sign PDF)
//        try {
//            File signedFile = pdfService.createSignedFile(new File("Invoice_ORD123.pdf"), "John Doe");
//            System.out.println("Signed PDF File generated: " + signedFile.getAbsolutePath());
//        } catch (Exception e) {
//            System.err.println("Error adding signature: " + e.getMessage());
//        }
//
//        String hash = pdfService.readHash(new File("src/main/java/com/spring/websellspringmvc/services/pdf/Invoice_ORD123.pdf"));
//        System.out.println("Hash: " + hash);

        // Ghi dữ liệu vào metadata của file signed_Invoice_ORD123.pdf
//        createPdf();
    }


    public static void createPdf() throws IOException {
        String inputHTML="D:\\university\\ATTT\\web-sell-spring-mvc\\src\\main\\resources\\templates\\templateEmailPlaceOrder.html";
        String outputPdf="D:\\university\\ATTT\\web-sell-spring-mvc\\src\\main\\resources\\templates\\templateEmailPlaceOrder.pdf";
        Document document = Jsoup.parse(new File(inputHTML), "UTF-8");
        try (OutputStream os = new FileOutputStream(outputPdf)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(outputPdf);
            builder.toStream(os);
            System.out.println(document.html());
            // If you need the file path, use getResource()
            URL fontUrl = Test.class.getResource("/templates/roboto.ttf");
            builder.useFont(new File(fontUrl.toURI()), "roboto");
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
            builder.run();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
