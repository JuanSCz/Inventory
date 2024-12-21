package com.interonda.inventory.controller;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.dto.VentaDTO;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableCompras")
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    private final ProductoService productoService;

    private final CompraService compraService;

    private final ProveedorService proveedorService;

    private CategoriaService categoriaService;

    private DepositoService depositoService;

    private final MessageSource messageSource;

    @Autowired
    public CompraController(ProductoService productoService, CompraService compraService, ProveedorService proveedorService, MessageSource messageSource, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.compraService = compraService;
        this.proveedorService = proveedorService;
        this.categoriaService = categoriaService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public String createProducto(@Valid ProductoDTO productoDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            model.addAttribute("productos", productos.getContent());
            model.addAttribute("productoDTO", productoDTO);
            model.addAttribute("page", productos);
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "tableProductos";
        }
        productoService.createProducto(productoDTO);
        return "redirect:/tableProductos";
    }

    @PostMapping("/update")
    public String updateCompra(@Valid CompraDTO compraDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("compraDTO", compraDTO);
            model.addAttribute("compras", compraService.getAllCompras(pageable).getContent());
            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            Pageable newPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            model.addAttribute("proveedores", proveedorService.getAllProveedores(newPageable, sort).getContent());
            return "tableCompras";
        }
        CompraDTO updatedCompraDTO = compraService.updateCompra(compraDTO);
        model.addAttribute("compraDTO", updatedCompraDTO);
        return "redirect:/tableCompras";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompra(@PathVariable Long id) {
        logger.debug("Llamando al método deleteCompra con id: {}", id);
        boolean isRemoved = compraService.deleteCompra(id);
        if (!isRemoved) {
            logger.warn("Compra con id: {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Compra con id: {} eliminada correctamente", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> getCompraById(@PathVariable Long id) {
        CompraDTO compraDTO = compraService.getCompra(id);
        return new ResponseEntity<>(compraDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showCompras(@RequestParam(required = false) String fecha, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("id").descending());
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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable proveedoresPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        model.addAttribute("proveedores", proveedorService.getAllProveedores(proveedoresPageable, sort).getContent());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        model.addAttribute("currentPage", "tableCompras");
        return "tableCompras";
    }

    @GetMapping("/search")
    public String searchCompras(@RequestParam(required = false) String nombreProveedor, Model model, Pageable pageable) {
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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable proveedoresPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        model.addAttribute("proveedores", proveedorService.getAllProveedores(proveedoresPageable, sort).getContent());
        return "tableCompras";
    }
}
