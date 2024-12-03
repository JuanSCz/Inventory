package com.interonda.inventory.controller;

import com.interonda.inventory.dto.UsuarioDTO;
import com.interonda.inventory.exceptions.AuthenticationException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
public class IndexController {

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "index"; // Nombre de la vista de login
    }

    @PostMapping("/login")
    public String login(@Valid UsuarioDTO usuarioDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining("<br>")); // Combina los mensajes de error
            model.addAttribute("errorMessage", errorMessage);
            return "index";
        }

        try {
            if ("admin".equals(usuarioDTO.getNombre()) && "password".equals(usuarioDTO.getContrasenia())) {
                return "redirect:/dashboard";
            } else {
                throw new AuthenticationException("Nombre de usuario o contraseña inválidos");
            }
        } catch (AuthenticationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "index";
        }
    }
}