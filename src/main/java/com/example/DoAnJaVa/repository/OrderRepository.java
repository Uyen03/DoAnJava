package com.example.DoAnJaVa.repository;

import com.example.DoAnJaVa.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByTxnRef(String txnRef);
    List<Order> findByEmail(String email);
    List<Order> findByCustomerName(String customerName);
}
