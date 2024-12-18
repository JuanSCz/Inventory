package com.interonda.inventory.controller;

import com.interonda.inventory.dto.CategoriaDTO;
import com.interonda.inventory.service.CategoriaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableCategorias")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    private final CategoriaService categoriaService;
    private final MessageSource messageSource;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, MessageSource messageSource) {
        this.categoriaService = categoriaService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public String createCategoria(@Valid CategoriaDTO categoriaDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("categorias", categoriaService.getAllCategorias(pageable).getContent());
            model.addAttribute("categoriaDTO", categoriaDTO);
            return "tableCategorias";
        }
        categoriaService.createCategoria(categoriaDTO);
        return "redirect:/tableCategorias";
    }

    @PostMapping("/update")
    public String updateCategoria(@Valid CategoriaDTO categoriaDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("categoriaDTO", categoriaDTO);
            model.addAttribute("categorias", categoriaService.getAllCategorias(pageable).getContent());
            return "tableCategorias";
        }
        categoriaService.updateCategoria(categoriaDTO);
        return "redirect:/tableCategorias";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        logger.debug("Llamando al método deleteCategoria con id: {}", id);
        boolean isRemoved = categoriaService.deleteCategoria(id);
        if (!isRemoved) {
            logger.warn("Categoria con id: {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Categoria con id: {} eliminada correctamente", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Long id) {
        CategoriaDTO categoriaDTO = categoriaService.getCategoria(id);
        return new ResponseEntity<>(categoriaDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showCategorias(@RequestParam(required = false) String name, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        Page<CategoriaDTO> categorias;
        if (name != null && !name.isEmpty()) {
            logger.info("Solicitud recibida para buscar categorias por nombre: {}", name);
            categorias = categoriaService.searchCategoriasByName(name, newPageable);
        } else {
            categorias = categoriaService.getAllCategorias(newPageable);
        }
        model.addAttribute("categorias", categorias.getContent());
        model.addAttribute("categoriaDTO", new CategoriaDTO());
        model.addAttribute("page", categorias);
        return "tableCategorias";
    }

    @GetMapping("/search")
    public String searchCategoriasByName(@RequestParam String name, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        logger.info("Solicitud recibida para buscar categorías por nombre: {}", name);
        Page<CategoriaDTO> categorias = categoriaService.searchCategoriasByName(name, newPageable);
        model.addAttribute("categorias", categorias.getContent());
        model.addAttribute("categoriaDTO", new CategoriaDTO());
        model.addAttribute("page", categorias);
        return "tableCategorias";
    }
}