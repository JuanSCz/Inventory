package com.interonda.inventory.controller;

import com.interonda.inventory.dto.HistorialStockDTO;
import com.interonda.inventory.service.HistorialStockService;
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
@RequestMapping("/api/historial-stock")
public class HistorialStockController {

    private static final Logger logger = LoggerFactory.getLogger(HistorialStockController.class);

    private final HistorialStockService historialStockService;

    @Autowired
    public HistorialStockController(HistorialStockService historialStockService) {
        this.historialStockService = historialStockService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HistorialStockDTO> createHistorialStock(@Valid @RequestBody HistorialStockDTO historialStockDTO) {
        logger.info("Solicitud recibida para crear historial de stock con detalles: {}", historialStockDTO);
        HistorialStockDTO createdHistorialStock = historialStockService.createHistorialStock(historialStockDTO);
        return new ResponseEntity<>(createdHistorialStock, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialStockDTO> updateHistorialStock(@PathVariable Long id, @Valid @RequestBody HistorialStockDTO historialStockDTO) {
        logger.info("Solicitud recibida para actualizar historial de stock con id: {}", id);
        HistorialStockDTO updatedHistorialStock = historialStockService.updateHistorialStock(id, historialStockDTO);
        return ResponseEntity.ok(updatedHistorialStock);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteHistorialStock(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar historial de stock con id: {}", id);
        historialStockService.deleteHistorialStock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<HistorialStockDTO>> getAllHistorialStock(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los historiales de stock");
        Page<HistorialStockDTO> historialStockPage = historialStockService.getAllHistorialStock(pageable);
        return ResponseEntity.ok(historialStockPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialStockDTO> getHistorialStockById(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener historial de stock con id: {}", id);
        HistorialStockDTO historialStock = historialStockService.getHistorialStock(id);
        return ResponseEntity.ok(historialStock);
    }
}