package com.example.DoAnJaVa.model;


import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRO")
    private int id;

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

    @Column(name = "IMGURL")
    private String imageURL;

//    @OneToMany(mappedBy = "product")
//    private Set<CartDetail> cartDetails;
}
