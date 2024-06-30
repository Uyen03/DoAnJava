package com.example.DoAnJaVa.AdminController;

import com.example.DoAnJaVa.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/contact-list")
public class AdminContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public String viewContacts(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "Admin/contact/contact-list";  // Updated path to the contact list
    }
}
