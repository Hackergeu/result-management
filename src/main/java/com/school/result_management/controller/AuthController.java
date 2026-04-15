package com.school.result_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // looks for templates/login.html
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";  // looks for templates/access-denied.html
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/login?logout=true";
    }
}