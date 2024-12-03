package com.interonda.inventory.controller;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductoDTO> createProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        logger.info("Solicitud recibida para crear producto con detalles: {}", productoDTO);
        ProductoDTO createdProducto = productoService.createProducto(productoDTO);
        return new ResponseEntity<>(createdProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        logger.info("Solicitud recibida para actualizar producto con id: {}", id);
        productoDTO.setId(id);
        ProductoDTO updatedProducto = productoService.updateProducto(productoDTO);
        return ResponseEntity.ok(updatedProducto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar producto con id: {}", id);
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener producto con id: {}", id);
        ProductoDTO producto = productoService.getProducto(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductoDTO>> getAllProductos(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los productos");
        Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
        return ResponseEntity.ok(productos);
    }
}