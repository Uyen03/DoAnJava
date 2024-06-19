package com.example.DoAnJaVa.AdminController;

import com.example.DoAnJaVa.service.ProductService;
import com.example.DoAnJaVa.service.CategoryService;
import com.example.DoAnJaVa.model.Category;
import com.example.DoAnJaVa.model.Product;
import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.Model;
//import com.example.DoAnJaVa.service.CategoryService;
//import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/product-list")
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());

        return "Admin/products/product-list";
    }

    // For adding a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Admin/products/add-product";
    }

    // Process the form for adding a new product
    @PostMapping("/add")
    public String addProduct( Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "Admin/products/add-product";
        }

        productService.addProduct(product);
        return "redirect:/admin/products/product-list";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {  Product product = productService.getProductById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Admin/products/update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, Product product, BindingResult result) {
        if (result.hasErrors()) {
            product.setId(Math.toIntExact(id));
            return "Admin/products/update-product";
        }

        productService.updateProduct(product);
        return "redirect:/admin/products/product-list";
    }

    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products/product-list";
    }


}