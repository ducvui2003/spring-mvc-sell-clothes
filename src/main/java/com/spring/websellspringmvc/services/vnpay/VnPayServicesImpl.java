package com.spring.websellspringmvc.services.vnpay;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VnPayServicesImpl implements VnPayServices {
    @NonFinal
    @Value("${app.service.vnpay.pay-url}")
    private String payUrl;

    @NonFinal
    @Value("${app.service.vnpay.pay-return-url}")
    private String payReturnUrl;

    @NonFinal
    @Value("${app.service.vnpay.tnn-code}")
    private String tnnCode;

    @NonFinal
    @Value("${app.service.vnpay.hash-secret}")
    private String hashSecret;

    @NonFinal
    @Value("${app.service.vnpay.api-url}")
    private String apiUrl;

    @Override
    public String generateUrl(double price, String paymentRef, String ip) throws UnsupportedEncodingException {
        // Constants for VNPAY parameters
        final String vnpVersion = "2.1.0";
        final String vnpCommand = "pay";
        final String orderType = "other";
        final long amount = (long) price * 100;

        // Prepare VNPAY parameters
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", tnnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", paymentRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toán đơn hàng:" + paymentRef);
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_ReturnUrl", payReturnUrl);
        vnpParams.put("vnp_IpAddr", ip);

        // Locale handling
        vnpParams.put("vnp_Locale", "vn");

        // Add creation and expiration timestamps
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        String createDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_CreateDate", createDate);

        calendar.add(Calendar.MINUTE, 15);
        String expireDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_ExpireDate", expireDate);

        // Sort parameters and build hash and query strings
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                // Append to hash data
                hashData.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                // Append to query string
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    hashData.append("&");
                    query.append("&");
                }
            }
        }

        // Generate secure hash
        String secureHash = hmacSHA512(hashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        // Return final payment URL
        return payUrl + "?" + query.toString();
    }

    private String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }
}
