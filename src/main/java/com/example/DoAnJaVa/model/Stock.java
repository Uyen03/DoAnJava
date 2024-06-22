package com.example.DoAnJaVa.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "Stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_STOCK")
    private Long id;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID_PRO")
    private Product product;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;
}
