package com.example.DoAnJaVa.controller;

import com.example.DoAnJaVa.service.CategoryService;
import com.example.DoAnJaVa.service.ProductService;
import com.example.DoAnJaVa.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private ProductService productService;


    @GetMapping("/")
    public String home(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        List<Product> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProductsByName(keyword);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        return "index";
    }


}
