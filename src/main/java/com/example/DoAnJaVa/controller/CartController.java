package com.example.DoAnJaVa.controller;

import com.example.DoAnJaVa.model.CartItem;
import com.example.DoAnJaVa.model.Product;
import com.example.DoAnJaVa.service.CartService;
import com.example.DoAnJaVa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.calculateTotalPrice());
        return "Admin/cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));

        if (quantity > product.getNums()) {
            redirectAttributes.addFlashAttribute("error", "Cannot add more items than available in stock");
            return "redirect:/products/" + productId;
        }

        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @ModelAttribute
    public void populateModel(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
    }
}
