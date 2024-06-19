package com.example.DoAnJaVa.controller;

import com.example.DoAnJaVa.model.User;
import com.example.DoAnJaVa.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller // Đánh dấu lớp này là một Controller trong Spring MVC.
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/login")
    public String login() {
        return "users/login";
    }
    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vàomodel
        return "users/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, @NotNull BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) { // Kiểm tra nếu có lỗi validate
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register"; // Trả về lại view "register" nếu có lỗi
        }
        userService.save(user); // Lưu người dùng vào cơ sở dữ liệu
        userService.setDefaultRole(user.getUsername()); // Gán vai trò mặc định cho người dùng
        return "redirect:/login"; // Chuyển hướng người dùng tới trang "login"
    }



    // Admin functionalities

    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users/list";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id).orElse(null);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/users/edit";
    }

    @PostMapping("/admin/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") User user, @NotNull BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "admin/users/edit";
        }
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

}
