package com.example.DoAnJaVa.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = "Giá không được nhỏ hơn 0")
    private double price;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    private String mainImage;
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Stock stock;
}
