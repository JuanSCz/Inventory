package com.interonda.inventory.controller;

import com.interonda.inventory.dto.UsuarioDTO;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.service.RolService;
import com.interonda.inventory.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableUsuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final MessageSource messageSource;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, RolService rolService, MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public String createUsuario(@Valid UsuarioDTO usuarioDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("usuarios", usuarioService.getAllUsuarios(pageable).getContent());
            model.addAttribute("usuarioDTO", usuarioDTO);
            model.addAttribute("page", usuarioService.getAllUsuarios(pageable));
            model.addAttribute("roles", rolService.getAllRoles(PageRequest.of(0, Integer.MAX_VALUE)).getContent()); // Asegúrate de que esta línea esté presente
            return "tableUsuarios";
        }
        usuarioService.createUsuario(usuarioDTO);
        return "redirect:/tableUsuarios";
    }

    @PostMapping("/update")
    public String updateUsuario(@Valid UsuarioDTO usuarioDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("usuarioDTO", usuarioDTO);
            model.addAttribute("usuarios", usuarioService.getAllUsuarios(pageable).getContent());
            model.addAttribute("roles", rolService.getAllRoles(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "tableUsuarios";
        }
        usuarioService.updateUsuario(usuarioDTO);
        return "redirect:/tableUsuarios";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        logger.debug("Llamando al método deleteUsuario con id: {}", id);
        boolean isRemoved = usuarioService.deleteUsuario(id);
        if (!isRemoved) {
            logger.warn("Usuario con id: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Usuario con id: {} eliminado correctamente", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = usuarioService.getUsuario(id);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }
}