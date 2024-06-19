package com.example.DoAnJaVa.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CAT")
    private int id;

    @Column(name = "NAME_CAT", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}
