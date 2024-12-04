package com.interonda.inventory.controller;

import com.interonda.inventory.dto.ProveedorDTO;
import com.interonda.inventory.service.ProveedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tableProveedores")
public class ProveedorController {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorController.class);

    private final ProveedorService proveedorService;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("proveedorDTO", new ProveedorDTO());
        return "fragments/formCreate :: formCreate";
    }

    @PostMapping
    public String createProveedor(ProveedorDTO proveedorDTO, Model model) {
        proveedorService.createProveedor(proveedorDTO);
        return "redirect:/tableProveedores";
    }

    @GetMapping
    public String getAllProveedores(Model model, Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los proveedores");
        Page<ProveedorDTO> proveedoresDTO = proveedorService.getAllProveedores(pageable);
        model.addAttribute("proveedores", proveedoresDTO.getContent());
        return "tableProveedores";
    }

    @GetMapping("/search")
    public String searchProveedoresByName(@RequestParam String name, Model model, Pageable pageable) {
        logger.info("Solicitud recibida para buscar proveedores por nombre: {}", name);
        Page<ProveedorDTO> proveedores = proveedorService.searchProveedoresByName(name, pageable);
        model.addAttribute("proveedores", proveedores.getContent());
        return "tableProveedores";
    }
}