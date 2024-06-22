package com.example.DoAnJaVa.service;

import com.example.DoAnJaVa.model.CartItem;
import com.example.DoAnJaVa.model.Order;
import com.example.DoAnJaVa.model.OrderDetail;
import com.example.DoAnJaVa.repository.OrderDetailRepository;
import com.example.DoAnJaVa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService
    @Transactional
    public Order createOrder(String customerName,String paymentMethod,String shippingMethod,String address,String email, List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setPaymentMethod(paymentMethod);
        order.setShippingMethod(shippingMethod);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setEmail(email);
        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        // Optionally clear the cart after order placement
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
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(newStatus);
            orderRepository.save(order);
        }
    }

    public double calculateTotalRevenue() {
//        List<Order> completedOrders = orderRepository.findByStatus("COMPLETED"); // Lấy các đơn hàng đã hoàn thành
//        List<Order> allOrders = orderRepository.findAll();
//        double totalRevenue = 0.0;
//        for (Order order : completedOrders) {
//            totalRevenue += order.getTotalPrice(); // Giả sử đã tính tổng giá trị của mỗi đơn hàng khi lưu vào CSDL
//        }
//        return totalRevenue;

        List<Order> allOrders = orderRepository.findAll(); // Lấy tất cả các đơn hàng
        double totalRevenue = 0.0;
        for (Order order : allOrders) {
            totalRevenue += order.getTotalPrice();
        }
        return totalRevenue;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order); // Lưu cập nhật đối tượng Order vào CSDL
    }

}
