package com.interonda.inventory.controller;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.dto.ProductoDepositoDTO;
import com.interonda.inventory.service.CategoriaService;
import com.interonda.inventory.service.DepositoService;
import com.interonda.inventory.service.DepositoProductoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableDepositoProducto")
public class DepositoProductoController {
    private static final Logger logger = LoggerFactory.getLogger(DepositoProductoController.class);

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final DepositoService depositoService;
    private final DepositoProductoService depositoProductoService;
    private final MessageSource messageSource;


    @Autowired
    public DepositoProductoController(ProductoService productoService, CategoriaService categoriaService, DepositoService depositoService, MessageSource messageSource, DepositoProductoService depositoProductoService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.depositoService = depositoService;
        this.depositoProductoService = depositoProductoService;
        this.messageSource = messageSource;
    }

    @PostMapping("/update")
    public String updateProducto(@Valid ProductoDepositoDTO productoDepositoDTO, BindingResult result, Model model, @RequestParam Long depositoId, Pageable pageable) {
        logger.debug("Entrando al método updateProducto con depositoProductoId: {}", depositoId);
        logger.debug("Datos del producto recibidos: {}", productoDepositoDTO);

        // Verificar los IDs en el DTO
        logger.debug("ID del producto en el DTO: {}", productoDepositoDTO.getId());
        logger.debug("ID del depósito en el DTO: {}", productoDepositoDTO.getDepositoId());

        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                    .collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            model.addAttribute("productos", productos.getContent());
            model.addAttribute("productoDTO", productoDepositoDTO);
            model.addAttribute("page", productos);
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            logger.debug("Errores de validación encontrados: {}", errorMessage);
            return "redirect:/main?table=tableDepositoProducto&depositoId=" + depositoId;
        }

        try {
            depositoProductoService.updateProducto(productoDepositoDTO);
            return "redirect:/main?table=tableDepositoProducto&depositoId=" + depositoId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error actualizando el producto: " + e.getMessage());
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            model.addAttribute("productos", productos.getContent());
            model.addAttribute("productoDTO", productoDepositoDTO);
            model.addAttribute("page", productos);
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "redirect:/main?table=tableDepositoProducto&depositoId=" + depositoId;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        logger.debug("Entrando al método getProductoById con id: {}", id);
        ProductoDTO productoDTO = productoService.getProducto(id);
        logger.debug("Producto encontrado: {}", productoDTO);
        return new ResponseEntity<>(productoDTO, HttpStatus.OK);
    }
}
