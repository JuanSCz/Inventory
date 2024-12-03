package com.interonda.inventory.controller;

import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.service.DetalleCompraService;
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
@RequestMapping("/api/detalles-compra")
public class DetalleCompraController {

    private static final Logger logger = LoggerFactory.getLogger(DetalleCompraController.class);

    private final DetalleCompraService detalleCompraService;

    @Autowired
    public DetalleCompraController(DetalleCompraService detalleCompraService) {
        this.detalleCompraService = detalleCompraService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DetalleCompraDTO> createDetalleCompra(@Valid @RequestBody DetalleCompraDTO detalleCompraDTO) {
        logger.info("Solicitud recibida para crear detalle de compra con detalles: {}", detalleCompraDTO);
        DetalleCompraDTO createdDetalleCompra = detalleCompraService.createDetalleCompra(detalleCompraDTO);
        return new ResponseEntity<>(createdDetalleCompra, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleCompraDTO> updateDetalleCompra(@PathVariable Long id, @Valid @RequestBody DetalleCompraDTO detalleCompraDTO) {
        logger.info("Solicitud recibida para actualizar detalle de compra con id: {}", id);
        DetalleCompraDTO updatedDetalleCompra = detalleCompraService.updateDetalleCompra(id, detalleCompraDTO);
        return new ResponseEntity<>(updatedDetalleCompra, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDetalleCompra(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar detalle de compra con id: {}", id);
        detalleCompraService.deleteDetalleCompra(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<DetalleCompraDTO>> getAllDetalleCompra(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los detalles de compra");
        Page<DetalleCompraDTO> detallesCompra = detalleCompraService.getAllDetalleCompra(pageable);
        return new ResponseEntity<>(detallesCompra, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompraDTO> getDetalleCompraById(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener detalle de compra con id: {}", id);
        DetalleCompraDTO detalleCompra = detalleCompraService.getDetalleCompraById(id);
        return new ResponseEntity<>(detalleCompra, HttpStatus.OK);
    }
}