package com.example.DoAnJaVa.controller;


import com.example.DoAnJaVa.model.Product;
import com.example.DoAnJaVa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("/all-product")
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());

        return "layout/all-product";
    }

    @GetMapping("/category/{categoryName}")
    public String viewProductsByCategory(@PathVariable String categoryName, Model model) {
        List<Product> products = productService.getProductsByCategory(categoryName);
        model.addAttribute("products", products);
        model.addAttribute("category", categoryName);
        return "layout/category-view";
    }
}
