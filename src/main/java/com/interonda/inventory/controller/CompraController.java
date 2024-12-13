package com.interonda.inventory.controller;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.entity.Compra;
import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.service.CompraService;
import com.interonda.inventory.service.ProductoService;
import com.interonda.inventory.service.ProveedorService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableCompras")
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);


    private final ProductoService productoService;


    private final CompraService compraService;


    private final ProveedorService proveedorService;


    private final MessageSource messageSource;

    @Autowired
    public CompraController(ProductoService productoService, CompraService compraService, ProveedorService proveedorService, MessageSource messageSource) {
        this.productoService = productoService;
        this.compraService = compraService;
        this.proveedorService = proveedorService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public String createCompra(@Valid CompraDTO compraDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                    .collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("compras", compraService.getAllCompras(pageable).getContent());
            model.addAttribute("compraDTO", compraDTO);
            return "tableCompras";
        }
        compraService.createCompra(compraDTO);
        return "redirect:/tableCompras";
    }

    @PostMapping("/update")
    public String updateCompra(@Valid CompraDTO compraDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                    .collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("compraDTO", compraDTO);
            model.addAttribute("compras", compraService.getAllCompras(pageable).getContent());
            model.addAttribute("proveedores", proveedorService.getAllProveedores(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "tableCompras";
        }
        CompraDTO updatedCompraDTO = compraService.updateCompra(compraDTO);
        model.addAttribute("compraDTO", updatedCompraDTO);
        return "redirect:/tableCompras";
    }

    @PostMapping("/{id}")
    public String deleteCompra(@PathVariable Long id) {
        logger.debug("Llamando al método deleteCompra con id: {}", id);
        compraService.deleteCompra(id);
        logger.debug("Compra con id: {} eliminada correctamente", id);
        return "redirect:/tableCompras";
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> getCompraById(@PathVariable Long id) {
        CompraDTO compraDTO = compraService.getCompra(id);
        return new ResponseEntity<>(compraDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showCompras(@RequestParam(required = false) String fecha, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        Page<CompraDTO> compras;
        if (fecha != null && !fecha.isEmpty()) {
            logger.info("Solicitud recibida para buscar compras por fecha: {}", fecha);
            compras = compraService.searchComprasByFecha(LocalDate.parse(fecha), newPageable);
        } else {
            compras = compraService.getAllCompras(newPageable);
        }
        model.addAttribute("compras", compras.getContent());
        model.addAttribute("compraDTO", new CompraDTO());
        model.addAttribute("page", compras);
        model.addAttribute("proveedores", proveedorService.getAllProveedores(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "tableCompras";
    }

    @GetMapping("/search")
    public String searchCompras(@RequestParam(required = false) String nombreProveedor,
                                Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        Page<CompraDTO> compras;

        if (nombreProveedor != null && !nombreProveedor.isEmpty()) {
            logger.info("Solicitud recibida para buscar compras por nombre de proveedor: {}", nombreProveedor);
            compras = compraService.searchComprasByProveedorNombre(nombreProveedor, newPageable);
        } else {
            compras = compraService.getAllCompras(newPageable);
        }

        model.addAttribute("compras", compras.getContent());
        model.addAttribute("compraDTO", new CompraDTO());
        model.addAttribute("page", compras);
        model.addAttribute("proveedores", proveedorService.getAllProveedores(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        return "tableCompras";
    }
}
