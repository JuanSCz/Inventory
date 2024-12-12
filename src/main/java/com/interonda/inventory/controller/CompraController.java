package com.interonda.inventory.controller;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.DetalleCompraDTO;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableCompras")
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;

    private final ProveedorService proveedorService;

    private final ProductoService productoService;

    private final MessageSource messageSource;

    @Autowired
    public CompraController(CompraService compraService, ProveedorService proveedorService, ProductoService productoService, MessageSource messageSource) {
        this.compraService = compraService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
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
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("compraDTO", compraDTO);
            model.addAttribute("compras", compraService.getAllCompras(pageable).getContent());
            return "tableCompras";
        }
        compraService.updateCompra(compraDTO);
        return "tableCompras";
    }

    @PostMapping("/{id}")
    public String deleteCompra(@PathVariable Long id) {
        logger.debug("Llamando al método deleteCompra con id: {}", id);
        compraService.deleteCompra(id);
        logger.debug("Compra con id: {} eliminada correctamente", id);
        return "tableCompras";
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
            LocalDate localDate = LocalDate.parse(fecha);
            compras = compraService.searchComprasByFecha(localDate, newPageable);
        } else {
            compras = compraService.getAllCompras(newPageable);
        }
        model.addAttribute("compras", compras.getContent());
        model.addAttribute("compraDTO", new CompraDTO());
        model.addAttribute("detalleCompraDTO", new DetalleCompraDTO());
        model.addAttribute("page", compras);

        // Agregar proveedores y productos al modelo
        model.addAttribute("proveedores", proveedorService.getAllProveedores(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        model.addAttribute("productos", productoService.getAllProductos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());

        return "tableCompras";
    }

    @PostMapping("/detalle")
    @ResponseBody
    public ResponseEntity<DetalleCompraDTO> addDetalleCompra(@Valid @RequestBody DetalleCompraDTO detalleCompraDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        DetalleCompraDTO savedDetalle = compraService.addDetalleCompra(detalleCompraDTO);
        return ResponseEntity.ok(savedDetalle);
    }

    @GetMapping("/search")
    public String searchComprasByFecha(@RequestParam String fecha, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        logger.info("Solicitud recibida para buscar compras por fecha: {}", fecha);
        LocalDate localDate = LocalDate.parse(fecha);
        Page<CompraDTO> compras = compraService.searchComprasByFecha(localDate, newPageable);
        model.addAttribute("compras", compras.getContent());
        model.addAttribute("compraDTO", new CompraDTO());
        model.addAttribute("page", compras);
        return "tableCompras";
    }
}