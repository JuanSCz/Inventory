package com.interonda.inventory.controller;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.service.CategoriaService;
import com.interonda.inventory.service.DepositoService;
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

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableProductos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final DepositoService depositoService;
    private final MessageSource messageSource;

    @Autowired
    public ProductoController(ProductoService productoService, CategoriaService categoriaService, DepositoService depositoService, MessageSource messageSource) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.depositoService = depositoService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public String createProducto(@Valid ProductoDTO productoDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            model.addAttribute("productos", productos.getContent());
            model.addAttribute("productoDTO", productoDTO);
            model.addAttribute("page", productos);
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "main?table=tableProductos";
        }

        try {
            productoDTO.setPrecio(new BigDecimal(productoDTO.getPrecioString().replace(".", "").replace(",", ".")));
            productoDTO.setCosto(new BigDecimal(productoDTO.getCostoString().replace(".", "").replace(",", ".")));
        } catch (NumberFormatException e) {
            bindingResult.rejectValue("precioString", "error.productoDTO", "Formato de precio inválido");
            bindingResult.rejectValue("costoString", "error.productoDTO", "Formato de costo inválido");
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            model.addAttribute("productos", productos.getContent());
            model.addAttribute("productoDTO", productoDTO);
            model.addAttribute("page", productos);
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "main?table=tableProductos";
        }

        productoService.createProducto(productoDTO);
        return "redirect:/main?table=tableProductos";
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
            return "main?table=tableProductos";
        }

        try {
            productoDTO.setPrecio(new BigDecimal(productoDTO.getPrecioString().replace(".", "").replace(",", ".")));
            productoDTO.setCosto(new BigDecimal(productoDTO.getCostoString().replace(".", "").replace(",", ".")));
        } catch (NumberFormatException e) {
            result.rejectValue("precioString", "error.productoDTO", "Formato de precio inválido");
            result.rejectValue("costoString", "error.productoDTO", "Formato de costo inválido");
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            model.addAttribute("productos", productos.getContent());
            model.addAttribute("productoDTO", productoDTO);
            model.addAttribute("page", productos);
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "main?table=tableProductos";
        }

        productoService.updateProducto(productoDTO);
        return "redirect:/main?table=tableProductos";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        logger.debug("Llamando al método deleteProducto con id: {}", id);
        boolean isRemoved = productoService.deleteProducto(id);
        if (!isRemoved) {
            logger.warn("Producto con id: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Producto con id: {} eliminado correctamente", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        ProductoDTO productoDTO = productoService.getProducto(id);
        return new ResponseEntity<>(productoDTO, HttpStatus.OK);
    }

}