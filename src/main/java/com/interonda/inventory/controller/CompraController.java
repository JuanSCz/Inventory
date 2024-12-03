package com.interonda.inventory.controller;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.service.CompraService;
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
@RequestMapping("/api/compras")
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompraDTO> createCompra(@Valid @RequestBody CompraDTO compraDTO) {
        logger.info("Solicitud recibida para crear compra con detalles: {}", compraDTO);
        CompraDTO createdCompra = compraService.createCompra(compraDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompra);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraDTO> updateCompra(@PathVariable Long id, @Valid @RequestBody CompraDTO compraDTO) {
        logger.info("Solicitud recibida para actualizar compra con id: {}", id);
        CompraDTO updatedCompra = compraService.updateCompra(id, compraDTO);
        return ResponseEntity.ok(updatedCompra);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCompra(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar compra con id: {}", id);
        compraService.deleteCompra(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CompraDTO>> getAllCompras(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todas las compras");
        Page<CompraDTO> compras = compraService.getAllCompras(pageable);
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> getCompraById(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener compra con id: {}", id);
        CompraDTO compra = compraService.getCompraById(id);
        return ResponseEntity.ok(compra);
    }
}