package com.example.DoAnJaVa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VnPayConfig {
    @Value("${vnpay.tmn_code}")
    private String tmnCode;

    @Value("${vnpay.hash_secret}")
    private String hashSecret;

    @Value("${vnpay.url}")
    private String vnpayUrl;

    @Value("${vnpay.return_url}")
    private String returnUrl;

    public String getTmnCode() {
        return tmnCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getVnpayUrl() {
        return vnpayUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }
}
