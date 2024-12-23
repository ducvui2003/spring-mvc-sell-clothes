package com.spring.websellspringmvc.services.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.spring.websellspringmvc.dto.response.AdminOrderDetailResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailItemResponse;
import com.spring.websellspringmvc.dto.response.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.IllegalFormatConversionException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {
    @Override
    public byte[] createDataFile(OrderDetailResponse detailResponse, List<AdminOrderDetailResponse> orderDetailResponse, String hash) {
        try {
            String htmlTemplate = loadHtmlTemplate();
            String filledHtml = populateHtmlTemplate(htmlTemplate, detailResponse);
            return generatePdfFromHtml(filledHtml);
        } catch (Exception e) {
            throw new RuntimeException("Error creating data file: " + e.getMessage(), e);
        }
    }
    @Override
    public File createFile( OrderDetailResponse detailResponse,  String hash) {
        try {
            String htmlTemplate = loadHtmlTemplate();
            String filledHtml = populateHtmlTemplate(htmlTemplate, detailResponse);
            File pdfFile = new File( detailResponse.getOrderId() + ".pdf");
            writePdfToFile(filledHtml, pdfFile);

            addMetadataToPdf(pdfFile, Map.of("Hash", hash));
            return pdfFile;
        }catch (IllegalFormatConversionException e) {
            throw new RuntimeException("Error creating file: " + e.getMessage(), e);

        }
        catch (Exception e) {
            throw new RuntimeException("Error creating PDF file: " + e.getMessage(), e);
        }
    }

    @Override
    public File createSignedFile(File orderFile, String signature) {
        try {
            File signedPdf = new File(orderFile.getParent(), "Signed_" + orderFile.getName());
            addMetadataToPdf(orderFile, Map.of("Signature", signature), signedPdf);
            return signedPdf;
        } catch (Exception e) {
            throw new RuntimeException("Error creating signed file: " + e.getMessage(), e);
        }
    }

    @Override
    public String readSignature(File signedFile) {
        return readMetadataFromPdf(signedFile, "Signature");
    }

    @Override
    public String readHash(File signedFile){
        return readMetadataFromPdf(signedFile, "Hash");
    }

    private void addMetadataToPdf(File inputPdf, Map<String, String> metadata, File outputPdf) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputPdf.getAbsolutePath());
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPdf));
        Map<String, String> existingMetadata = reader.getInfo();
        existingMetadata.putAll(metadata);
        stamper.setInfoDictionary(existingMetadata);
        stamper.close();
        reader.close();
    }

    // Overloaded method for inline metadata addition
    private void addMetadataToPdf(File pdfFile, Map<String, String> metadata) throws IOException, DocumentException {
        File tempFile = new File(pdfFile.getParent(), "Temp_" + pdfFile.getName());
        addMetadataToPdf(pdfFile, metadata, tempFile);

        if (!pdfFile.delete() || !tempFile.renameTo(pdfFile)) {
            throw new IOException("Failed to replace original PDF file with updated metadata.");
        }
    }

    // Utility for reading metadata
    private String readMetadataFromPdf(File pdfFile, String key) {
        try (PdfReader reader = new PdfReader(pdfFile.getAbsolutePath())) {
            Map<String, String> metadata = reader.getInfo();
            return metadata.getOrDefault(key, "").trim();
        } catch (IOException e) {
            throw new RuntimeException("Error reading metadata from PDF: " + e.getMessage(), e);
        }
    }

    private String populateHtmlTemplate(String template, OrderDetailResponse detailResponse) {
        List<OrderDetailItemResponse> items = detailResponse.getItems();
        double tempPrice = 0;
        for (OrderDetailItemResponse item : items) {
            tempPrice += item.getPrice() * item.getQuantity();
            System.out.println(item.getPrice());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

        // Kiểm tra các giá trị không phải String và chuyển đổi chúng
        String invoiceDate = detailResponse.getDateOrder() != null ?  detailResponse.getDateOrder().format(formatter): "";
        String totalItem = String.valueOf(items.size());
        String tempPriceFormatted = formatVND(tempPrice);
        String discountPriceFormatted = formatVND(0);
        String shippingFeeFormatted = formatVND(detailResponse.getFee());
        String totalPriceFormatted = formatVND(detailResponse.getFee() + tempPrice);
        String orderStatus = detailResponse.getStatus();

//        String paymentStatus = orderDetailResponses.isEmpty() || orderDetailResponses.getFirst().getTransactionStatus() == null
//                ? ""
//                : switch (orderDetailResponses.getFirst().getTransactionStatus()) {
//            case UN_PAID -> "Chưa thanh toán";
//            case PROCESSING -> "Đang xử lý";
//            case PAID -> "Đã thanh toán";
//            case ERROR -> "Lỗi giao dịch";
//        };

        // Thay thế các giá trị trong template
        return template
                .replace("%%INVOICENO%%", detailResponse.getOrderId())
                .replace("%%INVOICEDATE%%", invoiceDate)
                .replace("%%FULLNAME%%", detailResponse.getFullName())
                .replace("%%EMAIL%%", detailResponse.getEmail())
                .replace("%%PHONE%%", detailResponse.getPhone())
                .replace("%%ADDRESS%%", detailResponse.getProvince() + ", " + detailResponse.getDistrict() + ", " + detailResponse.getWard() + ", " + detailResponse.getDetail())
                .replace("%%TOTALITEM%%", totalItem)
                .replace("%%TEMPPRICE%%", tempPriceFormatted)
                .replace("%%DISCOUNTPRICE%%", discountPriceFormatted)
                .replace("%%SHIPPINGFEE%%", shippingFeeFormatted)
                .replace("%%TOTALPRICE%%", totalPriceFormatted)
                .replace("%%DELIVERYMETHOD%%", "Giao hàng nhanh") // Placeholder
                .replace("%%PAYMENTMETHOD%%", detailResponse.getPayment())
                .replace("%%ITEMSBOUGHT%%", generateItemsTable(detailResponse.getItems()))
                .replace("%%ORDERSTATUS%%", orderStatus);
//                .replace("%%PAYMENTSTATUS%%", paymentStatus);
    }

    private String generateItemsTable(List<OrderDetailItemResponse> items) {
        StringBuilder sb = new StringBuilder();
        for (OrderDetailItemResponse item : items) {
            sb.append("<tr>")
                    .append("<td style=\"border: 1px solid #d9d9d9; text-align: left; padding: 8px;\">")
                    .append(item.getName())
                    .append("</td>")
                    .append("<td style=\"border: 1px solid #d9d9d9; text-align: center; padding: 8px;\">")
                    .append(formatVND(item.getPrice()))
                    .append("</td>")
                    .append("<td style=\"border: 1px solid #d9d9d9; text-align: center; padding: 8px;\">")
                    .append(item.getQuantity())
                    .append("</td>")
                    .append("<td style=\"border: 1px solid #d9d9d9; text-align: right; padding: 8px;\">")
                    .append(formatVND(item.getPrice() * item.getQuantity()))
                    .append("</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String loadHtmlTemplate() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/templateEmailPlaceOrder.html");
        if (inputStream == null) {
            throw new FileNotFoundException("Template not found: templates/templateEmailPlaceOrder.html");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            return content.toString().trim();
        }
    }

    private byte[] generatePdfFromHtml(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.toStream(outputStream);

            // Load font
            URL fontUrl = getClass().getClassLoader().getResource("templates/roboto.ttf");
            if (fontUrl == null) {
                throw new FileNotFoundException("Font file not found: templates/roboto.ttf");
            }
            builder.useFont(new File(fontUrl.toURI()), "roboto");

            // Parse HTML and set to builder
            Document document = Jsoup.parse(html, "UTF-8");
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");

            // Render PDF
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
        return outputStream.toByteArray();
    }

    private void writePdfToFile(String html, File outputFile) {
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.toStream(outputStream);

            // Load font
            URL fontUrl = getClass().getClassLoader().getResource("templates/roboto.ttf");
            if (fontUrl == null) {
                throw new FileNotFoundException("Font file not found: templates/roboto.ttf");
            }
            builder.useFont(new File(fontUrl.toURI()), "roboto");

            // Parse HTML and set to builder
            Document document = Jsoup.parse(html, "UTF-8");
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");

            // Render PDF
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("Error writing PDF file: " + e.getMessage(), e);
        }
    }

    private String formatVND(double amount) {
        // Định dạng tiền tệ cho Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Định dạng lại để loại bỏ ký hiệu đồng tiền quốc tế (ví dụ: VND)
        String formatted = currencyFormat.format(amount);

        // Thay "VND" bằng "đ" để phù hợp với yêu cầu
        formatted = formatted.replace("VND", "đ");

        return formatted;
    }


    // Read HTML file with UTF-8 encoding
    private static String readHtmlAsUtf8(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }
        return contentBuilder.toString();
    }

}

