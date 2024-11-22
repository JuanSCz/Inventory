package com.interonda.Inventory.controller;

import com.interonda.Inventory.dto.DepositoDTO;
import com.interonda.Inventory.serviceImplTest.DepositoService;
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
@RequestMapping("/api/depositos")
public class DepositoController {

    private static final Logger logger = LoggerFactory.getLogger(DepositoController.class);

    private final DepositoService depositoService;

    @Autowired
    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DepositoDTO> createDeposito(@Valid @RequestBody DepositoDTO depositoDTO) {
        logger.info("Solicitud recibida para crear depósito con detalles: {}", depositoDTO);
        DepositoDTO createdDeposito = depositoService.createDeposito(depositoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeposito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepositoDTO> updateDeposito(@PathVariable Long id, @Valid @RequestBody DepositoDTO depositoDTO) {
        logger.info("Solicitud recibida para actualizar depósito con id: {}", id);
        DepositoDTO updatedDeposito = depositoService.updateDeposito(id, depositoDTO);
        return ResponseEntity.ok(updatedDeposito);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDeposito(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar depósito con id: {}", id);
        depositoService.deleteDeposito(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<DepositoDTO>> getAllDepositos(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los depósitos");
        Page<DepositoDTO> depositos = depositoService.getAllDepositos(pageable);
        return ResponseEntity.ok(depositos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositoDTO> getDepositoById(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener depósito con id: {}", id);
        DepositoDTO deposito = depositoService.getDepositoById(id);
        return ResponseEntity.ok(deposito);
    }
}