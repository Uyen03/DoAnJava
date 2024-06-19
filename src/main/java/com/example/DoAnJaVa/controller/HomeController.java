package com.example.DoAnJaVa.controller;

import com.example.DoAnJaVa.service.ProductService;
import com.example.DoAnJaVa.model.Product;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private ProductService productService;

    @GetMapping
    public String home(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }


}
