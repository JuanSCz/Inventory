package com.interonda.Inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password,
                        Model model) {
        if ("admin".equals(name) && "password".equals(password)) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Nombre de usuario o contraseña inválidos");
            return "login";
        }
    }
}