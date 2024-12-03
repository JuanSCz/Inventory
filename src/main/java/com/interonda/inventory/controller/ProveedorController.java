package com.interonda.inventory.controller;

import com.interonda.inventory.dto.ProveedorDTO;
import com.interonda.inventory.service.ProveedorService;
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
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorController.class);

    private final ProveedorService proveedorService;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProveedorDTO> createProveedor(@Valid @RequestBody ProveedorDTO proveedorDTO) {
        logger.info("Solicitud recibida para crear proveedor con detalles: {}", proveedorDTO);
        ProveedorDTO createdProveedor = proveedorService.createProveedor(proveedorDTO);
        return new ResponseEntity<>(createdProveedor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> updateProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorDTO proveedorDTO) {
        logger.info("Solicitud recibida para actualizar proveedor con id: {}", id);
        proveedorDTO.setId(id);
        ProveedorDTO updatedProveedor = proveedorService.updateProveedor(proveedorDTO);
        return ResponseEntity.ok(updatedProveedor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar proveedor con id: {}", id);
        proveedorService.deleteProveedor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> getProveedorById(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener proveedor con id: {}", id);
        ProveedorDTO proveedor = proveedorService.getProveedor(id);
        return ResponseEntity.ok(proveedor);
    }

    @GetMapping
    public ResponseEntity<Page<ProveedorDTO>> getAllProveedores(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los proveedores");
        Page<ProveedorDTO> proveedores = proveedorService.getAllProveedores(pageable);
        return ResponseEntity.ok(proveedores);
    }
}