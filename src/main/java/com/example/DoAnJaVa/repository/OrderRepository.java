package com.example.DoAnJaVa.repository;

import com.example.DoAnJaVa.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByTxnRef(String txnRef);
    List<Order> findByOrderDate(LocalDate orderDate); // Add this method
}
