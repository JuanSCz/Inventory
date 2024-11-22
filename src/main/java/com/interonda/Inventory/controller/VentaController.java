package com.interonda.Inventory.controller;

import com.interonda.Inventory.dto.VentaDTO;
import com.interonda.Inventory.serviceImplTest.VentaService;
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
@RequestMapping("/api/ventas")
public class VentaController {

    private static final Logger logger = LoggerFactory.getLogger(VentaController.class);

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VentaDTO> createVenta(@Valid @RequestBody VentaDTO ventaDTO) {
        logger.info("Solicitud recibida para crear venta con detalles: {}", ventaDTO);
        VentaDTO createdVenta = ventaService.createVenta(ventaDTO);
        return new ResponseEntity<>(createdVenta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> updateVenta(@PathVariable Long id, @Valid @RequestBody VentaDTO ventaDTO) {
        logger.info("Solicitud recibida para actualizar venta con id: {}", id);
        if (!id.equals(ventaDTO.getId())) {
            logger.warn("El id de la URL no coincide con el id del cuerpo de la solicitud");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        VentaDTO updatedVenta = ventaService.updateVenta(ventaDTO);
        return new ResponseEntity<>(updatedVenta, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar venta con id: {}", id);
        ventaService.deleteVenta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> getVenta(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener venta con id: {}", id);
        VentaDTO ventaDTO = ventaService.getVenta(id);
        return new ResponseEntity<>(ventaDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<VentaDTO>> getAllVentas(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todas las ventas");
        Page<VentaDTO> ventas = ventaService.getAllVentas(pageable);
        return new ResponseEntity<>(ventas, HttpStatus.OK);
    }
}