// src/main/java/com/interonda/Inventory/controller/IndexController.java
package com.interonda.Inventory.controller;

import com.interonda.Inventory.dto.UsuarioDTO;
import com.interonda.Inventory.exceptions.AuthenticationException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "index";
    }

    @PostMapping("/login")
    public String login(@Valid UsuarioDTO usuarioDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        try {
            if ("admin".equals(usuarioDTO.getNombre()) && "password".equals(usuarioDTO.getContrasenia())) {
                return "redirect:/dashboard";
            } else {
                throw new AuthenticationException("Nombre de usuario o contraseña inválidos");
            }
        } catch (AuthenticationException e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }
}