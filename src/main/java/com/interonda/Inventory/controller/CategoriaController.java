package com.interonda.Inventory.controller;

import com.interonda.Inventory.dto.CategoriaDTO;
import com.interonda.Inventory.serviceImplTest.CategoriaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoriaDTO> crearCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        logger.info("Solicitud recibida para crear categoría con nombre: {}", categoriaDTO.getNombre());
        CategoriaDTO createdCategoria = categoriaService.createCategoria(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@PathVariable Long id, @Validated @RequestBody CategoriaDTO categoriaDTO) {
        logger.info("Solicitud recibida para actualizar categoría con id: {}", id);
        CategoriaDTO updatedCategoria = categoriaService.updateCategoria(id, categoriaDTO);
        return ResponseEntity.ok(updatedCategoria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoria(@PathVariable Long id) {
        logger.info("Solicitud recibida para eliminar categoría con id: {}", id);
        categoriaService.deleteCategoria(id);
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaDTO>> getAllCategorias(Pageable pageable) {
        logger.info("Solicitud recibida para obtener todas las categorías");
        Page<CategoriaDTO> categorias = categoriaService.getAllCategorias(pageable);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Long id) {
        logger.info("Solicitud recibida para obtener categoría con id: {}", id);
        CategoriaDTO categoria = categoriaService.getCategoriaById(id);
        return ResponseEntity.ok(categoria);
    }
}