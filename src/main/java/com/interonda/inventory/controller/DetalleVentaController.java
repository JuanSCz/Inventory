package com.interonda.inventory.controller;

import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.service.DetalleVentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/detallesVentas")
public class DetalleVentaController {

    private static final Logger logger = LoggerFactory.getLogger(DetalleVentaController.class);

    private final DetalleVentaService detalleVentaService;

    @Autowired
    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<DetalleVentaDTO> getDetalleVentaById(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener detalle de venta con id: {}", id);
        DetalleVentaDTO detalleVenta = detalleVentaService.getDetalleVentaById(id);
        return new ResponseEntity<>(detalleVenta, HttpStatus.OK);
    }

    @GetMapping("/{ventaId}")
    public ResponseEntity<List<DetalleVentaDTO>> getDetallesByVentaId(@PathVariable Long ventaId) {
        List<DetalleVentaDTO> detallesVenta = detalleVentaService.getDetallesByVentaId(ventaId);
        return new ResponseEntity<>(detallesVenta, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DetalleVentaDTO> createDetalleVenta(@Valid @RequestBody DetalleVentaDTO detalleVentaDTO) {
        logger.info("Solicitud recibida para crear detalle de venta con detalles: {}", detalleVentaDTO);
        DetalleVentaDTO createdDetalleVenta = detalleVentaService.createDetalleVenta(detalleVentaDTO);
        return new ResponseEntity<>(createdDetalleVenta, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDetalleVenta(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar detalle de venta con id: {}", id);
        detalleVentaService.deleteDetalleVenta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<DetalleVentaDTO>> getAllDetalleVenta(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los detalles de venta");
        Page<DetalleVentaDTO> detallesVenta = detalleVentaService.getAllDetalleVenta(pageable);
        return new ResponseEntity<>(detallesVenta, HttpStatus.OK);
    }
}
