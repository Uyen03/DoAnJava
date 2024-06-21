package com.example.DoAnJaVa.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CAT", referencedColumnName = "ID_CAT")
    private Category category;

    @Column(name = "NAME_PRO", nullable = false)
    private String name;

    @Column(name = "NUMS", nullable = false)
    private int nums;

    @Column(name = "PRICE", nullable = false)
    private double price;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    private String mainImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<ProductImage> ProductImage;  // Danh sách ảnh sản phẩm

}
