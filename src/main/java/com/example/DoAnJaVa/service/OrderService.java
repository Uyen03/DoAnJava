package com.example.DoAnJaVa.service;
import com.example.DoAnJaVa.model.CartItem;
import com.example.DoAnJaVa.model.Order;
import com.example.DoAnJaVa.model.OrderDetail;
import com.example.DoAnJaVa.repository.OrderDetailRepository;
import com.example.DoAnJaVa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Transactional
    public Order createOrder(String customerName, String paymentMethod, String shippingMethod, String address, String email, List<CartItem> cartItems, String txnRef) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setPaymentMethod(paymentMethod);
        order.setShippingMethod(shippingMethod);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setEmail(email);
        order.setTotalPrice(cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum());
        order.setStatus("Pending");
        order.setTxnRef(txnRef); // Set transaction reference
        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);

            // Giảm số lượng sản phẩm trong kho
            productService.reduceStock(item.getProduct().getId(), item.getQuantity());
        }

        // Clear the cart after order placement
        cartService.clearCart();

        return order;
    }

    // Method to get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Method to get order by ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // Method to update order status
    public void updateOrderStatus(String txnRef, String newStatus) {
        Order order = orderRepository.findByTxnRef(txnRef);
        if (order != null) {
            order.setStatus(newStatus);
            orderRepository.save(order);
        }
    }

    // Method to calculate total revenue
    public double calculateTotalRevenue() {
        List<Order> allOrders = orderRepository.findAll(); // Lấy tất cả các đơn hàng
        double totalRevenue = 0.0;
        for (Order order : allOrders) {
            totalRevenue += order.getTotalPrice();
        }
        return totalRevenue;
    }

    // Method to save order
    public void saveOrder(Order order) {
        orderRepository.save(order); // Lưu cập nhật đối tượng Order vào CSDL
    }

    // Method to get orders for the current user

    public List<Order> getOrdersForCurrentUser() {
        // Lấy thông tin người dùng hiện tại từ context bảo mật của Spring Security
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Giả sử bạn có một phương thức trong OrderRepository để lấy đơn hàng theo tên người dùng
        return orderRepository.findByCustomerName(username);
    }
}
