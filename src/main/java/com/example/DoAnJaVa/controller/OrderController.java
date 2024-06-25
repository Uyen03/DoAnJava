package com.example.DoAnJaVa.controller;

import com.example.DoAnJaVa.model.CartItem;
import com.example.DoAnJaVa.service.CartService;
import com.example.DoAnJaVa.service.OrderService;
import com.example.DoAnJaVa.service.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private VnPayService vnPayService;

    @GetMapping("/checkout")
    public String checkout() {
        return "/Admin/cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(@RequestParam String customerName,
                              @RequestParam String paymentMethod,
                              @RequestParam String shippingMethod,
                              @RequestParam String address,
                              @RequestParam String email,
                              RedirectAttributes redirectAttributes) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }

        // Xử lý khi người dùng chọn phương thức thanh toán VNPay
        if (paymentMethod.equals("vnpay")) {
            try {
                Double totalAmount = cartService.calculateTotalPrice();
                String paymentUrl = vnPayService.createPaymentUrl(totalAmount, "Order #" + System.currentTimeMillis());
                return "redirect:" + paymentUrl;
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo URL thanh toán VNPay");
                return "redirect:/order/checkout";
            }
        }

        // Xử lý các phương thức thanh toán khác
        orderService.createOrder(customerName, paymentMethod, shippingMethod, address, email, cartItems);
        return "redirect:/order/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "/Admin/cart/order-confirmation";
    }

    @GetMapping("/vnpay_return")
    public String vnpayReturn(@RequestParam Map<String, String> params, Model model) {
        // Xử lý kết quả trả về từ VNPay
        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            model.addAttribute("message", "Thanh toán thành công!");
        } else {
            model.addAttribute("message", "Thanh toán thất bại!");
        }
        return "/Admin/cart/order-confirmation";
    }
    @GetMapping("/payment/vnpay")
    public String createPayment(@RequestParam Double amount, @RequestParam String orderInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String paymentUrl = vnPayService.createPaymentUrl(amount, orderInfo);
        return "redirect:" + paymentUrl;
    }

    @GetMapping("/payment/vnpay_return")
    @ResponseBody
    public String handlePaymentReturn(@RequestParam Map<String, String> params) {
        // Xử lý phản hồi thanh toán từ VNPay
        return "Payment Successful";
    }
}
