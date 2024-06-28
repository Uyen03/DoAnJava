package com.example.DoAnJaVa.AdminController;

import com.example.DoAnJaVa.model.CartItem;
import com.example.DoAnJaVa.model.Order;
import com.example.DoAnJaVa.model.OrderDetail;
import com.example.DoAnJaVa.service.CartService;
import com.example.DoAnJaVa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.number.CurrencyStyleFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
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

            orderService.saveOrder(order);
        }
        model.addAttribute("orders", orders);
        return "Admin/orders/orders"; // Đường dẫn tương đối tới tệp orders.html
    }

    @GetMapping("/order-details/{orderId}")
    public String viewOrderDetails(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "Admin/orders/order-details"; // Đường dẫn tương đối tới tệp order-details.html
    }

    @PostMapping("/{orderId}/updateStatus")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam String newStatus) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            orderService.updateOrderStatus(order.getTxnRef(), newStatus);
        }
        return "redirect:/admin/orders/order-details/" + orderId;
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

    @GetMapping("/revenue")
    public String calculateTotalRevenue(Model model) {
        double totalRevenue = orderService.calculateTotalRevenue();

        CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();
        Locale locale = LocaleContextHolder.getLocale();
        String formattedTotalRevenue = formatter.print(totalRevenue, locale);
        model.addAttribute("totalRevenue", totalRevenue);
        return "Admin/orders/revenue"; // Đường dẫn tương đối tới view hiển thị tổng doanh thu
    }
}
