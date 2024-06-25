package com.example.DoAnJaVa.service;

import com.example.DoAnJaVa.VnPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnPayService {

    @Autowired
    private VnPayConfig vnPayConfig;

    public String createPaymentUrl(Double amount, String orderInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String vnp_TxnRef = UUID.randomUUID().toString();
        String vnp_OrderInfo = orderInfo;
        String vnp_OrderType = "billpayment";
        String vnp_Amount = String.format("%.0f", amount * 100);
        String vnp_Locale = "vn";
        String vnp_ReturnUrl = vnPayConfig.getReturnUrl();
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = vnPayConfig.getTmnCode();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                // Build query string
                query.append(fieldName);
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }
        String vnp_SecureHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        query.append("&vnp_SecureHashType=HmacSHA512");
        query.append("&vnp_SecureHash=");
        query.append(URLEncoder.encode(vnp_SecureHash, StandardCharsets.UTF_8.toString()));
        return vnPayConfig.getVnpayUrl() + "?" + query.toString();
    }

    private String hmacSHA512(String key, String data) throws NoSuchAlgorithmException {
        try {
            byte[] hmacData;
            byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(byteKey, "HmacSHA512");
            mac.init(secretKey);
            hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : hmacData) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA-512", e);
        }
    }
}
