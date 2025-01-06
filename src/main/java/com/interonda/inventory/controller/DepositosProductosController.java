package com.interonda.inventory.controller;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.service.CategoriaService;
import com.interonda.inventory.service.DepositoService;
import com.interonda.inventory.service.DepositosProductosService;
import com.interonda.inventory.service.ProductoService;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableDepositosProductos")
public class DepositosProductosController {
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final DepositoService depositoService;
    private final MessageSource messageSource;
    private final DepositosProductosService depositosProductosService;

    @Autowired
    public DepositosProductosController(ProductoService productoService, CategoriaService categoriaService, DepositoService depositoService, MessageSource messageSource, DepositosProductosService depositosProductosService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.depositoService = depositoService;
        this.messageSource = messageSource;
        this.depositosProductosService = depositosProductosService;
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
            return "tableDepositosProductos";
        }

        productoService.updateProducto(productoDTO);
        return "redirect:/tableDepositosProductos";
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
        return "fragments/productosTable :: productosTableBody";
    }

    @GetMapping
    public String getProductosPorDeposito(@RequestParam(required = false) Long depositoId, Model model, Pageable pageable) {
        if (depositoId == null) {
            // Si no se proporciona un depósito, selecciona el primer depósito disponible
            depositoId = depositoService.getAllDepositos(PageRequest.of(0, 1)).getContent().get(0).getId();
        }
        Page<ProductoDTO> productos = depositosProductosService.getProductosByDeposito(depositoId, pageable);
        model.addAttribute("productos", productos.getContent());
        model.addAttribute("page", productos);
        model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        model.addAttribute("selectedDepositoId", depositoId);
        model.addAttribute("productoDTO", new ProductoDTO()); // Añadir productoDTO al modelo
        return "tableDepositosProductos";
    }
}
