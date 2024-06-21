package com.example.DoAnJaVa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productImages")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_path")  // Đặt tên cột theo quy ước chuẩn
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "product_id")  // Đặt tên cột rõ ràng hơn
    private Product product;
}

