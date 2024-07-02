package com.example.DoAnJaVa.service;

import com.example.DoAnJaVa.model.Order;
import com.example.DoAnJaVa.model.OrderDetail;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private OrderService orderService;


    public ByteArrayInputStream exportRevenueToExcel(Map<LocalDate, Double> dailyRevenue, Map<YearMonth, Double> monthlyRevenue) throws IOException {
        String[] columns = {"Ngày", "Doanh thu", "Sản phẩm"};
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Doanh thu hàng ngày");

            // Create a merged region for the large header
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.length - 1)); // From row 0 to row 0, from col 0 to last column
            Row headerRow = sheet.createRow(0);
            Cell mergedCell = headerRow.createCell(0);
            mergedCell.setCellValue("DOANH THU CỬA HÀNG BÁN PHỤ KIỆN");
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            mergedCell.setCellStyle(headerStyle);

            // Create a header row
            Row subTitleRow = sheet.createRow(1);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = subTitleRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Create data rows
            int rowIdx = 2; // Start after the subtitle row
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("###,###,###"));

            for (Map.Entry<LocalDate, Double> entry : dailyRevenue.entrySet()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(entry.getKey().toString()); // Ngày

                Cell revenueCell = row.createCell(1);
                revenueCell.setCellValue(entry.getValue()); // Doanh thu
                revenueCell.setCellStyle(currencyStyle); // Định dạng số tiền

                // Add information about products in the order
                List<Order> orders = orderService.getOrdersByDate(entry.getKey());
                StringBuilder productsInfo = new StringBuilder();
                for (Order order : orders) {
                    for (OrderDetail detail : order.getOrderDetails()) {
                        String productInfo = String.format("%s: %d x %,.0f\n", detail.getProduct().getName(), detail.getQuantity(), detail.getProduct().getPrice());
                        productsInfo.append(productInfo);
                    }
                }
                Cell productsCell = row.createCell(2);
                productsCell.setCellValue(productsInfo.toString().trim()); // Sản phẩm
                productsCell.setCellStyle(currencyStyle); // Định dạng số tiền cho cột Sản phẩm
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}













//@Service
//public class ReportService {
//
//    @Autowired
//    private OrderService orderService;
//
//    public ByteArrayInputStream exportRevenueToExcel(Map<LocalDate, Double> dailyRevenue, Map<YearMonth, Double> monthlyRevenue) throws IOException {
//        String[] columns = {"Ngày", "Doanh thu", "Sản phẩm", "Số lượng", "Đơn giá"};
//        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            Sheet sheet = workbook.createSheet("Doanh thu hàng ngày");
//
//            // Create a header row
//            Row headerRow = sheet.createRow(0);
//            for (int i = 0; i < columns.length; i++) {
//                Cell cell = headerRow.createCell(i);
//                cell.setCellValue(columns[i]);
//            }
//
//            // Create data rows
//            int rowIdx = 1;
//            CellStyle currencyStyle = workbook.createCellStyle();
//            DataFormat format = workbook.createDataFormat();
//            currencyStyle.setDataFormat(format.getFormat("_-[$₫-vi-VN]* #,##0.00_-;\\-[$₫-vi-VN]* #,##0.00_-;_-[$₫-vi-VN]* \"-\"??_-;_-@_-"));
//
//            for (Map.Entry<LocalDate, Double> entry : dailyRevenue.entrySet()) {
//                Row row = sheet.createRow(rowIdx++);
//                row.createCell(0).setCellValue(entry.getKey().toString()); // Ngày
//                row.createCell(1).setCellValue(entry.getValue()); // Doanh thu
//
//                // Add information about products in the order
//                List<Order> orders = orderService.getOrdersByDate(entry.getKey());
//                StringBuilder productsInfo = new StringBuilder();
//                for (Order order : orders) {
//                    for (OrderDetail detail : order.getOrderDetails()) {
//                        productsInfo.append(detail.getProduct().getName()).append(": ")
//                                .append(detail.getQuantity()).append(" x ");
//
//                        // Create cell for quantity
//                        Cell quantityCell = row.createCell(3);
//                        quantityCell.setCellValue(detail.getQuantity());
//
//                        // Create cell for price per unit
//                        Cell priceCell = row.createCell(4);
//                        priceCell.setCellValue(detail.getProduct().getPrice());
//                        priceCell.setCellStyle(currencyStyle); // Apply currency style to price
//                    }
//                }
//                row.createCell(2).setCellValue(productsInfo.toString().trim()); // Sản phẩm
//            }
//
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        }
//    }
//}