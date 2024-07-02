package com.example.DoAnJaVa.service;

import com.example.DoAnJaVa.model.Product;
import com.example.DoAnJaVa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.UDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById((long) product.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " + product.getId() + " does not exist."));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setMainImage(product.getMainImage());
        existingProduct.setCategory(product.getCategory());
        return productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    // Thêm phương thức tìm kiếm sản phẩm theo tên
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public void reduceStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product with ID " + productId + " does not exist."));
        if (product.getNums() >= quantity) {
            product.setNums(product.getNums() - quantity);
            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Không đủ hàng tồn kho");
        }
    }

    public List<String> getProductNamesArray(){
        List<String> names = new ArrayList<>();
        List<Product> products = getAllProducts();

        for (Product items : products){
            if (items.getName() != null){
                String temp ="'" + items.getName() + "'";
                names.add(temp);
            }
            else
                names.add("''");
        }

        return names;
    }

    public List<String> getProductImagesArray(){
        List<String> images = new ArrayList<>();
        List<Product> products = getAllProducts();

        for (Product items : products){
            if (items.getMainImage() != null){
                String temp ="'" + items.getMainImage() + "'";
                images.add(temp);
            }
            else
                images.add("''");
        }

        return images;
    }

    public List<Double> getProductPricesArray(){
        List<Double> prices = new ArrayList<>();
        List<Product> products = getAllProducts();

        for (Product items : products){
            if (items.getPrice() != 0)
                prices.add(items.getPrice());
            else
                prices.add((double) 0);
        }

        return prices;
    }
}
