package com.example.DoAnJaVa.service;

import com.example.DoAnJaVa.model.ProductImage;
import com.example.DoAnJaVa.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public List<ProductImage> getAllProductImages() {
        return productImageRepository.findAll();
    }

    public void addProductImage(ProductImage productImage) {
        productImageRepository.save(productImage);
    }
}
