package com.example.DoAnJaVa.repository;

import com.example.DoAnJaVa.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByProductId(Long productId);
}
