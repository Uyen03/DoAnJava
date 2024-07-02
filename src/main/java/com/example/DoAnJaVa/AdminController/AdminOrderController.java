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

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        orderService.updateOrderStatus(orderId, newStatus);
        return "redirect:/admin/orders/" + orderId;
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

    @GetMapping("/revenue/daily")
    public String getDailyRevenue(Model model) {
        Map<LocalDate, Double> dailyRevenue = orderService.calculateDailyRevenue();
        model.addAttribute("dailyRevenue", dailyRevenue);
        return "Admin/orders/daily-revenue"; // Đường dẫn tới view hiển thị doanh thu hàng ngày
    }

    @GetMapping("/revenue/monthly")
    public String getMonthlyRevenue(Model model) {
        Map<YearMonth, Double> monthlyRevenue = orderService.calculateMonthlyRevenue();
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        return "Admin/orders/monthly-revenue"; // Đường dẫn tới view hiển thị doanh thu hàng tháng
    }
    @GetMapping("/revenue/summary")
    public String getRevenueSummary(Model model) {
        double totalRevenue = orderService.calculateTotalRevenue();
        Map<LocalDate, Double> dailyRevenue = orderService.calculateDailyRevenue();
        Map<YearMonth, Double> monthlyRevenue = orderService.calculateMonthlyRevenue();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("dailyRevenue", dailyRevenue);
        model.addAttribute("monthlyRevenue", monthlyRevenue);

        return "Admin/orders/revenue-summary";
    }



}
