package com.example.DoAnJaVa.AdminController;

import com.example.DoAnJaVa.model.Category;
import com.example.DoAnJaVa.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;

    @GetMapping("/category-list")
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "Admin/categories/category-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "Admin/categories/add-category";
    }

    @PostMapping("/add")
    public String addCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "Admin/categories/add-category";
        }
        categoryService.addCategory(category);
        return "redirect:category-list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "Admin/categories/update-category";
    }

    // POST request to update category 
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid Category category,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            category.setId(Math.toIntExact(id));
            return "Admin/categories/update-category";
        }
        categoryService.updateCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/admin/categories/category-list";
    }

    // GET request for deleting category 
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        categoryService.deleteCategoryById(id);
        return "redirect:category-list";
    }
}