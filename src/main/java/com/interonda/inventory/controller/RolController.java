package com.interonda.inventory.controller;

import com.interonda.inventory.dto.RolDTO;
import com.interonda.inventory.service.RolService;
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
@RequestMapping("/roles")
public class RolController {

    private static final Logger logger = LoggerFactory.getLogger(RolController.class);

    private final RolService rolService;

    @Autowired
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RolDTO> createRol(@Valid @RequestBody RolDTO rolDTO) {
        logger.info("Solicitud recibida para crear rol con detalles: {}", rolDTO);
        RolDTO createdRol = rolService.createRol(rolDTO);
        return new ResponseEntity<>(createdRol, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> updateRol(@PathVariable Long id, @Valid @RequestBody RolDTO rolDTO) {
        logger.info("Solicitud recibida para actualizar rol con id: {}", id);
        RolDTO updatedRol = rolService.updateRol(id, rolDTO);
        return new ResponseEntity<>(updatedRol, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar rol con id: {}", id);
        rolService.deleteRol(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getRol(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener rol con id: {}", id);
        RolDTO rolDTO = rolService.getRol(id);
        return new ResponseEntity<>(rolDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<RolDTO>> getAllRoles(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los roles");
        Page<RolDTO> roles = rolService.getAllRoles(pageable);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}