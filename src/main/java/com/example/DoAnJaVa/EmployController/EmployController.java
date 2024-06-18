package com.example.DoAnJaVa.EmployController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employ")
public class EmployController {
    @GetMapping
    public String employ(){
        return "Employ/EmployLayout";
    }
}
