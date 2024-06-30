package com.example.DoAnJaVa.EmployController;

import com.example.DoAnJaVa.model.CartItem;
import com.example.DoAnJaVa.model.Order;
import com.example.DoAnJaVa.model.OrderDetail;
import com.example.DoAnJaVa.service.CartService;
import com.example.DoAnJaVa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.number.CurrencyStyleFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/employ/orders")
public class EmployOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

    // New methods for admin order management
    @GetMapping
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            double totalPrice = calculateOrderTotalPrice(order);
            order.setTotalPrice(totalPrice);
        }
        model.addAttribute("orders", orders);
        return "Employ/orders/orders"; // Đường dẫn tương đối tới tệp order-details.html
    }

    @GetMapping("/order-details/{orderId}")
    public String viewOrderDetails(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "Employ/orders/order-details"; // Đường dẫn tương đối tới tệp order-details.html
    }

    @PostMapping("/{orderId}/updateStatus")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam String newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        return "redirect:/employ/orders/" + orderId;
    }

    // Helper method to calculate total price for an order
    private double calculateOrderTotalPrice(Order order) {
        double totalPrice = 0.0;
        List<OrderDetail> orderDetails = order.getOrderDetails(); // Assuming this retrieves order details

        for (OrderDetail detail : orderDetails) {
            totalPrice += detail.getQuantity() * detail.getProduct().getPrice(); // Calculate total price for each detail
        }

        return totalPrice;
    }

    @ModelAttribute
    public void populateModel(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
    }
}
