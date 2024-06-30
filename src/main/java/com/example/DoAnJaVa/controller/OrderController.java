package com.example.DoAnJaVa.controller;

import com.example.DoAnJaVa.model.CartItem;
import com.example.DoAnJaVa.model.Order;
import com.example.DoAnJaVa.service.CartService;
import com.example.DoAnJaVa.service.OrderService;
import com.example.DoAnJaVa.service.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
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
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }

        // Lưu thông tin vào session để sử dụng sau khi thanh toán thành công
        session.setAttribute("customerName", customerName);
        session.setAttribute("paymentMethod", paymentMethod);
        session.setAttribute("shippingMethod", shippingMethod);
        session.setAttribute("address", address);
        session.setAttribute("email", email);
        session.setAttribute("cartItems", cartItems);

        // Xử lý khi người dùng chọn phương thức thanh toán VNPay
        if (paymentMethod.equals("vnpay")) {
            try {
                Double totalAmount = cartService.calculateTotalPrice();
                String txnRef = "Order-" + System.currentTimeMillis();
                session.setAttribute("txnRef", txnRef); // Save txnRef in session

                String paymentUrl = vnPayService.createPaymentUrl(totalAmount, txnRef);
                return "redirect:" + paymentUrl;
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo URL thanh toán VNPay");
                return "redirect:/order/checkout";
            }
        }

        // Xử lý các phương thức thanh toán khác
        orderService.createOrder(customerName, paymentMethod, shippingMethod, address, email, cartItems, null);
        return "redirect:/order/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "/Admin/cart/order-confirmation";
    }

    @GetMapping("/payment/vnpay")
    public String createPayment(@RequestParam Double amount, @RequestParam String orderInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String paymentUrl = vnPayService.createPaymentUrl(amount, orderInfo);
        return "redirect:" + paymentUrl;
    }

    @GetMapping("/payment/vnpay_return")
    public String handlePaymentReturn(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            // Lấy thông tin từ session
            String customerName = (String) session.getAttribute("customerName");
            String paymentMethod = (String) session.getAttribute("paymentMethod");
            String shippingMethod = (String) session.getAttribute("shippingMethod");
            String address = (String) session.getAttribute("address");
            String email = (String) session.getAttribute("email");
            List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
            String txnRef = params.get("vnp_TxnRef");

            if (customerName != null && paymentMethod != null && shippingMethod != null && address != null && email != null && cartItems != null) {
                // Lưu đơn hàng và cập nhật trạng thái
                saveOrder(customerName, paymentMethod, shippingMethod, address, email, cartItems, txnRef);

                // Xóa thông tin trong session sau khi lưu đơn hàng
                session.removeAttribute("customerName");
                session.removeAttribute("paymentMethod");
                session.removeAttribute("shippingMethod");
                session.removeAttribute("address");
                session.removeAttribute("email");
                session.removeAttribute("cartItems");
                session.removeAttribute("txnRef");

                // Don cart sau khi thuc hien dat hang
                cartService.clearCart();
                model.addAttribute("message", "Thanh toán thành công!");
            } else {
                model.addAttribute("message", "Thông tin đơn hàng không đầy đủ!");
            }
        } else {
            model.addAttribute("message", "Thanh toán thất bại!");
        }
        return "/Admin/cart/order-confirmation";
    }

    private void saveOrder(String customerName, String paymentMethod, String shippingMethod, String address, String email, List<CartItem> cartItems, String txnRef) {
        orderService.createOrder(customerName, paymentMethod, shippingMethod, address, email, cartItems, txnRef);
        orderService.updateOrderStatus(txnRef, "Completed");  // Cập nhật trạng thái đơn hàng ngay sau khi tạo
    }

    @GetMapping("/view")
    public String viewOrders(Model model) {
        List<Order> orders = orderService.getOrdersForCurrentUser();
        model.addAttribute("orders", orders);
        return "/Admin/cart/orders"; // Tên của file HTML hiển thị danh sách đơn hàng
    }

    @GetMapping("/view/{id}")
    public String viewOrderDetails(@PathVariable("id") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "/Admin/cart/order-details"; // Tên của file HTML hiển thị chi tiết đơn hàng
    }
}
