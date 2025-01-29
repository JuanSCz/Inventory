package com.interonda.inventory.controller;

import com.interonda.inventory.dto.DepositoDTO;
import com.interonda.inventory.service.DepositoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableDepositos")
public class DepositoController {
    private static final Logger logger = LoggerFactory.getLogger(DepositoController.class);

    private final DepositoService depositoService;
    private final MessageSource messageSource;

    @Autowired
    public DepositoController(DepositoService depositoService, MessageSource messageSource) {
        this.depositoService = depositoService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public String createDeposito(@Valid DepositoDTO depositoDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<DepositoDTO> depositos = depositoService.getAllDepositos(pageable);
            model.addAttribute("depositos", depositoService.getAllDepositos(pageable).getContent());
            model.addAttribute("depositoDTO", depositoDTO);
            model.addAttribute("page", depositos);
            return "main?table=tableDepositos";
        }
        depositoService.createDeposito(depositoDTO);
        return "redirect:/main?table=tableDepositos";
    }

    @PostMapping("/update")
    public String updateDeposito(@Valid DepositoDTO depositoDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<DepositoDTO> depositos = depositoService.getAllDepositos(pageable);
            model.addAttribute("depositoDTO", depositoDTO);
            model.addAttribute("depositos", depositoService.getAllDepositos(pageable).getContent());
            model.addAttribute("page", depositos);
            return "main?table=tableDepositos";
        }
        depositoService.updateDeposito(depositoDTO);
        return "redirect:/main?table=tableDepositos";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeposito(@PathVariable Long id) {
        logger.debug("Llamando al método deleteDeposito con id: {}", id);
        boolean isRemoved = depositoService.deleteDeposito(id);
        if (!isRemoved) {
            logger.warn("Deposito con id: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Deposito con id: {} eliminado correctamente", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositoDTO> getDepositoById(@PathVariable Long id) {
        DepositoDTO depositoDTO = depositoService.getDeposito(id);
        return new ResponseEntity<>(depositoDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showDepositos(@RequestParam(required = false) String name, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("id").descending());
        Page<DepositoDTO> depositos;
        if (name != null && !name.isEmpty()) {
            logger.info("Solicitud recibida para buscar depositos por nombre: {}", name);
            depositos = depositoService.searchDepositosByName(name, newPageable);
        } else {
            depositos = depositoService.getAllDepositos(newPageable);
        }
        model.addAttribute("depositos", depositos.getContent());
        model.addAttribute("depositoDTO", new DepositoDTO());
        model.addAttribute("page", depositos);
        model.addAttribute("currentPage", "tableDepositos");
        return "main?table=tableDepositos";
    }

    @GetMapping("/search")
    public String searchDepositosByName(@RequestParam String name, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        logger.info("Solicitud recibida para buscar depositos por nombre: {}", name);
        Page<DepositoDTO> depositos = depositoService.searchDepositosByName(name, newPageable);
        model.addAttribute("depositos", depositos.getContent());
        model.addAttribute("depositoDTO", new DepositoDTO());
        model.addAttribute("page", depositos);
        return "main?table=tableDepositos";
    }
}