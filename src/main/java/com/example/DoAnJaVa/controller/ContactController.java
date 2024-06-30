package com.example.DoAnJaVa.controller;

import com.example.DoAnJaVa.model.Contact;
import com.example.DoAnJaVa.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "Admin/contact/contact";
    }

    @PostMapping("/contact")
    public String submitContactForm(Contact contact) {
        contactService.saveContact(contact);
        return "redirect:/contact?success";
    }
}
