package com.interonda.inventory.controller;

import com.interonda.inventory.dto.HistorialStockDTO;
import com.interonda.inventory.service.HistorialStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tableHistorialStock")
public class HistorialStockController {

    private static final Logger logger = LoggerFactory.getLogger(HistorialStockController.class);

    private final HistorialStockService historialStockService;

    @Autowired
    public HistorialStockController(HistorialStockService historialStockService) {
        this.historialStockService = historialStockService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialStockDTO> getHistorialStock(@PathVariable Long id) {
        HistorialStockDTO historialStockDTO = historialStockService.getHistorialStock(id);
        return ResponseEntity.ok(historialStockDTO);
    }

    @GetMapping
    public String getAllHistorialStock(Model model, Pageable pageable) {
        Page<HistorialStockDTO> historialStockPage = historialStockService.getAllHistorialStock(pageable);
        model.addAttribute("historiales", historialStockPage.getContent());
        model.addAttribute("page", historialStockPage);
        return "main?table=tableHistorialStock";
    }
}