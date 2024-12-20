package com.spring.websellspringmvc.services.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.*;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {
    @Override
    public byte[] createDataFile(OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse) {
        try {
            // Load the HTML template
            String htmlTemplate = loadHtmlTemplate();

            // Fill the placeholders with data
            String filledHtml = populateHtmlTemplate(htmlTemplate, detailResponse, orderDetailResponse);

            // Convert to PDF
            return generatePdfFromHtml(filledHtml);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public File createFile(OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse) {
        try {
            // Load the HTML template
            String htmlTemplate = loadHtmlTemplate();

            // Fill the placeholders with data
            String filledHtml = populateHtmlTemplate(htmlTemplate, detailResponse, orderDetailResponse);


            // Convert to PDF
            File pdfFile = new File("Invoice_" + detailResponse.getOrderId() + ".pdf");
            writePdfToFile(filledHtml, pdfFile);

            return pdfFile;
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    @Override
    public File createSignedFile(File orderFile, String signature) throws IOException {
        File signedPdf = new File("signed_" + orderFile.getName());
        try {
            // Đọc file PDF đầu vào
            PdfReader reader = new PdfReader(orderFile.getAbsolutePath());

            // Tạo PdfStamper để ghi PDF mới với metadata
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(signedPdf));

            // Lấy thông tin metadata hiện tại
            HashMap<String, String> info = (HashMap<String, String>) reader.getInfo();

            // Thêm chữ ký vào metadata
            info.put("Signature", signature);

            // Cập nhật metadata vào file PDF
            stamper.setMoreInfo(info);

            // Đóng stamper và reader
            stamper.close();
            reader.close();
        } catch (IOException | DocumentException e) {
            throw new RuntimeException("Error adding signature metadata to PDF", e);
        }
        return signedPdf;
    }

    @Override
    public String readSignature(File signedFile) {
        try {
            // Đọc file PDF
            PdfReader reader = new PdfReader(signedFile.getAbsolutePath());

            // Lấy thông tin metadata
            HashMap<String, String> metadata = (HashMap<String, String>) reader.getInfo();

            // Đóng file sau khi đọc
            reader.close();

            // Lấy giá trị chữ ký từ metadata
            String signature = metadata.get("Signature");

            // Kiểm tra và trả về chữ ký (nếu có)
            if (signature != null && signature.startsWith("Signature: ")) {
                return signature.replace("Signature: ", "");
            } else {
                return "No signature found in the PDF metadata.";
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading signature from PDF", e);
        }
    }


    private String loadHtmlTemplate() throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource("templates/templateEmailPlaceOrder.html");
        if (resourceUrl == null) {
            throw new FileNotFoundException("Template not found");
        }
        InputStream inputStream = resourceUrl.openStream();
        return new String(inputStream.readAllBytes());
    }

    private String populateHtmlTemplate(String template, OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponses) {
        return template
                .replace("%%INVOICENO%%", detailResponse.getOrderId())
                .replace("%%INVOICEDATE%%", detailResponse.getDateOrder().toString())
                .replace("%%FULLNAME%%", detailResponse.getFullName())
                .replace("%%EMAIL%%", detailResponse.getEmail())
                .replace("%%PHONE%%", detailResponse.getPhone())
                .replace("%%ADDRESS%%", detailResponse.getProvince() + ", " + detailResponse.getDistrict() + ", " + detailResponse.getWard() + ", " + detailResponse.getDetail())
                .replace("%%TOTALITEM%%", String.valueOf(detailResponse.getItems().size()))
                .replace("%%TEMPPRICE%%", String.format("%.2f", detailResponse.getFee()))
                .replace("%%DISCOUNTPRICE%%", "0.00") // Placeholder
                .replace("%%SHIPPINGFEE%%", "50.00") // Placeholder
                .replace("%%TOTALPRICE%%", String.format("%.2f", detailResponse.getFee() + 50.00)) // Fee + shipping
                .replace("%%DELIVERYMETHOD%%", "Standard Shipping") // Placeholder
                .replace("%%PAYMENTMETHOD%%", detailResponse.getPayment())
                .replace("%%ITEMSBOUGHT%%", generateItemsTable(detailResponse.getItems())
                .replace("%%ORDERSTATUS%%", orderDetailResponses.get(0).getOrderStatus().toString()) // Thay thế trạng thái đơn hàng
                .replace("%%PAYMENTSTATUS%%", orderDetailResponses.get(0).getTransactionStatus().toString()));// Thay thế trạng thái thanh toán);
    }

    private String generateItemsTable(List<OrderDetailItemResponse> items) {
        StringBuilder sb = new StringBuilder();
        for (OrderDetailItemResponse item : items) {
            sb.append("<tr>")
                    .append("<td style=\"border: 1px solid #d9d9d9; text-align: left; padding: 8px;\">")
                    .append(item.getName())
                    .append("</td>")
                    .append("<td style=\"border: 1px solid #d9d9d9; text-align: center; padding: 8px;\">")
                    .append(String.format("%.2f", item.getPrice()))
                    .append("</td>")
                    .append("<td style=\"border: 1px solid #d9d9d9; text-align: center; padding: 8px;\">")
                    .append(item.getQuantity())
                    .append("</td>")
                    .append("<td style=\"border: 1px solid #d9d9d9; text-align: right; padding: 8px;\">")
                    .append(String.format("%.2f", item.getPrice() * item.getQuantity()))
                    .append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    public String replaceOrderStatuses(String htmlTemplate, AdminOrderDetailResponse detail) {
        // Thay thế các giá trị placeholder trong HTML
        return htmlTemplate
                .replace("%%ORDERSTATUS%%", detail.getOrderStatus().toString()) // Thay thế trạng thái đơn hàng
                .replace("%%PAYMENTSTATUS%%", detail.getTransactionStatus().toString()) // Thay thế trạng thái thanh toán
                .replace("%%SHIPPINGFEE%%", String.format("%.2f", detail.getFee())); // Thay thế phí vận chuyển
    }

    private byte[] generatePdfFromHtml(String html) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        Document document = Jsoup.parse(html, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        renderer.setDocumentFromString(document.html());
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }

    private void writePdfToFile(String html, File outputFile) throws DocumentException, IOException {
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            ITextRenderer renderer = new ITextRenderer();
            Document document = Jsoup.parse(html, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            renderer.setDocumentFromString(document.html());
            renderer.layout();
            renderer.createPDF(outputStream);
        }
    }
}

