package com.spring.websellspringmvc.services.vnpay;

import java.io.UnsupportedEncodingException;

public interface VnPayServices {
    String generateUrl(double amount, String paymentRef, String ip) throws UnsupportedEncodingException;
}
