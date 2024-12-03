package com.interonda.inventory.controller;

import com.interonda.inventory.dto.UsuarioDTO;
import com.interonda.inventory.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        logger.info("Solicitud recibida para crear usuario con nombre: {}", usuarioDTO.getNombre());
        UsuarioDTO createdUsuario = usuarioService.createUsuario(usuarioDTO);
        return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @Validated @RequestBody UsuarioDTO usuarioDTO) {
        logger.info("Solicitud recibida para actualizar usuario con id: {}", id);
        UsuarioDTO updatedUsuario = usuarioService.updateUsuario(id, usuarioDTO);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar usuario con id: {}", id);
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener usuario con id: {}", id);
        UsuarioDTO usuarioDTO = usuarioService.getUsuario(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<UsuarioDTO>> getAllUsuarios(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todos los usuarios");
        Page<UsuarioDTO> usuarios = usuarioService.getAllUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }
}
