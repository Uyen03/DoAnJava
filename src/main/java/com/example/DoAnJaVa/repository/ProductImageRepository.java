package com.example.DoAnJaVa.repository;
import com.example.DoAnJaVa.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {
}
