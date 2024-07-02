package com.example.DoAnJaVa.AdminController;

import com.example.DoAnJaVa.model.ProductImage;
import com.example.DoAnJaVa.service.ProductService;
import com.example.DoAnJaVa.service.CategoryService;
import com.example.DoAnJaVa.service.ProductImageService;
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
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;

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
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("mainImage") MultipartFile mainImage,   @RequestParam("productimages") MultipartFile[] imageList) throws IOException {

        if (!mainImage.isEmpty()) {
            try {
                String imageName = saveImageStatic(mainImage);
                product.setMainImage("/img/" + imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productService.addProduct(product);
        for (MultipartFile image : imageList) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = saveImageStatic(image);
                    ProductImage productImage = new ProductImage();
                    productImage.setImagePath("/img/" +imageUrl);
                    productImage.setProduct(product);
                    product.getImages().add(productImage);
                    productImageService.addProductImage(productImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/admin/products/product-list";
    }
    private String saveImageStatic(MultipartFile image) throws IOException {
//        File saveFile = new ClassPathResource("static/img").getFile();
//        String fileName = UUID.randomUUID()+ "." + StringUtils.getFilenameExtension(image.getOriginalFilename());
//        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);
//        Files.copy(image.getInputStream(), path);
//        return fileName;
        Path dirImages = Paths.get("target/classes/static/img");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }

        String newFileName = UUID.randomUUID()+ "." + StringUtils.getFilenameExtension(image.getOriginalFilename());

        Path pathFileUpload = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), pathFileUpload,
                StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Long id, Model model) {  Product product = productService.getProductById(id)
//            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
//        model.addAttribute("product", product);
//        model.addAttribute("categories", categoryService.getAllCategories());
//        return "Admin/products/update-product";
//    }

//    @PostMapping("/update/{id}")
//    public String updateProduct(@PathVariable Long id, Product product, BindingResult result) {
//        if (result.hasErrors()) {
//            product.setId(id);
//            return "Admin/products/update-product";
//        }
//
//        productService.updateProduct(product);
//        return "redirect:/admin/products/product-list";
//    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id sản phẩm không hợp lệ:" + id));

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Admin/products/update-product";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product, BindingResult result,
                                @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
                                @RequestParam(value = "productimages", required = false) MultipartFile[] imageList) throws IOException {

        if (result.hasErrors()) {
            product.setId(id); // Đảm bảo ID sản phẩm được đặt cho biểu mẫu
            return "Admin/products/update-product";
        }

        // Xử lý cập nhật ảnh chính
        if (mainImage != null && !mainImage.isEmpty()) {
            String imageName = saveImageStatic(mainImage);
            product.setMainImage("/img/" + imageName);
        }

        // Xử lý cập nhật ảnh bổ sung
        if (imageList != null) {
            for (MultipartFile image : imageList) {
                if (!image.isEmpty()) {
                    String imageUrl = saveImageStatic(image);
                    ProductImage productImage = new ProductImage();
                    productImage.setImagePath("/img/" + imageUrl);
                    productImage.setProduct(product);
                    product.getImages().add(productImage);
                    productImageService.addProductImage(productImage);
                }
            }
        }

        productService.updateProduct(product);
        return "redirect:/admin/products/product-list";
    }


    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/Admin/products/product-list";
    }

    @GetMapping("/detail/{id}")
    public String showProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/Admin/products/product-detail";
    }
}