package com.example.DoAnJaVa.EmployController;


import com.example.DoAnJaVa.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employ/contact-list")
public class EmployContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public String viewContacts(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "Employ/contact/contact-list";  // Updated path to the contact list
    }
}
