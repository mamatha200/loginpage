package com.example.authsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Principal p, Model model) {
        model.addAttribute("username", p.getName());
        return "user";
    }

    @GetMapping("/admin")
    public String admin(Principal p, Model model) {
        model.addAttribute("username", p.getName());
        return "admin";
    }
}
