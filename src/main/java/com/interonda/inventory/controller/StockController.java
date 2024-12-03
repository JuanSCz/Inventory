package com.interonda.inventory.controller;

import com.interonda.inventory.dto.StockDTO;
import com.interonda.inventory.service.StockService;
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
@RequestMapping("/api/stocks")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StockDTO> createStock(@Valid @RequestBody StockDTO stockDTO) {
        logger.info("Solicitud recibida para crear stock con detalles: {}", stockDTO);
        StockDTO createdStock = stockService.createStock(stockDTO);
        return new ResponseEntity<>(createdStock, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long id, @Valid @RequestBody StockDTO stockDTO) {
        logger.info("Solicitud recibida para actualizar stock con id: {}", id);
        stockDTO.setId(id);
        StockDTO updatedStock = stockService.updateStock(stockDTO);
        return new ResponseEntity<>(updatedStock, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar stock con id: {}", id);
        stockService.deleteStock(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStock(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener stock con id: {}", id);
        StockDTO stockDTO = stockService.getStock(id);
        return new ResponseEntity<>(stockDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<StockDTO>> getAllStocks(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los stocks");
        Page<StockDTO> stockPage = stockService.getAllStocks(pageable);
        return new ResponseEntity<>(stockPage, HttpStatus.OK);
    }
}
