package com.example.DoAnJaVa.service;

import com.example.DoAnJaVa.model.CartItem;
import com.example.DoAnJaVa.model.Product;
import com.example.DoAnJaVa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        cartItems.add(new CartItem(product, quantity));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double calculateTotalPrice() {
        double totalPrice = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        return Math.round(totalPrice);
    }


    public boolean updateQuantity(Long productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                int availableStock = item.getProduct().getNums(); // Lấy số lượng tồn kho de ktra
                if (quantity > availableStock) {
                    return false;
                }
                item.setQuantity(quantity);
                return true;
            }
        }
        return false;
    }

}
