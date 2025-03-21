package com.interonda.inventory.controller;

import com.interonda.inventory.dto.DepositoDTO;
import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableDepositosProductosUnidad")
public class DepositosProductosUnidadController {
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final DepositoService depositoService;
    private final MessageSource messageSource;
    private final DepositosProductosUnidadService depositosProductosUnidadService;

    @Autowired
    public DepositosProductosUnidadController(ProductoService productoService, CategoriaService categoriaService, DepositoService depositoService, MessageSource messageSource, DepositosProductosUnidadService depositosProductosUnidadService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.depositoService = depositoService;
        this.messageSource = messageSource;
        this.depositosProductosUnidadService = depositosProductosUnidadService;
    }

    @PostMapping("/update")
    public String updateProducto(@Valid ProductoDTO productoDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            model.addAttribute("productos", productos.getContent());
            model.addAttribute("productoDTO", productoDTO);
            model.addAttribute("page", productos);
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "tableDepositosProductosUnidad";
        }

        productoService.updateProducto(productoDTO);
        return "redirect:/tableDepositosProductosUnidad";
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        ProductoDTO productoDTO = productoService.getProducto(id);
        return new ResponseEntity<>(productoDTO, HttpStatus.OK);
    }

    @GetMapping("/search")
    public String searchProductosByName(@RequestParam String name, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        logger.info("Solicitud recibida para buscar productos por nombre: {}", name);
        Page<ProductoDTO> productos = productoService.searchProductosByName(name, newPageable);
        model.addAttribute("productos", productos.getContent());
        model.addAttribute("productoDTO", new ProductoDTO());
        model.addAttribute("page", productos);
        return "fragments/productosUnidadTable :: productosTableBody";
    }

    @GetMapping
    public String getProductosPorDeposito(@RequestParam(required = false) Long depositoId, Model model, Pageable pageable) {
        if (depositoId == null) {
            // Verifica si hay depósitos disponibles
            Page<DepositoDTO> depositosPage = depositoService.getAllDepositos(PageRequest.of(0, 1));
            if (depositosPage.isEmpty()) {
                // Maneja el caso en que no hay depósitos disponibles
                model.addAttribute("errorMessage", "No hay depósitos disponibles.");
                return "errorPage"; // Asegúrate de tener una página de error adecuada
            }
            // Selecciona el primer depósito disponible
            depositoId = depositosPage.getContent().get(0).getId();
        }

        Page<ProductoDTO> productos = depositosProductosUnidadService.getProductosByDeposito(depositoId, pageable);
        model.addAttribute("productos", productos.getContent());
        model.addAttribute("page", productos);
        model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        model.addAttribute("selectedDepositoId", depositoId);
        model.addAttribute("productoDTO", new ProductoDTO());
        model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());

        return "tableDepositosProductosUnidad";
    }
}

