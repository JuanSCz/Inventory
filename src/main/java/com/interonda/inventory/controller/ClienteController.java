package com.interonda.inventory.controller;

import com.interonda.inventory.dto.ClienteDTO;
import com.interonda.inventory.service.ClienteService;
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
@RequestMapping("/tableClientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService clienteService;
    private final MessageSource messageSource;

    @Autowired
    public ClienteController(ClienteService clienteService, MessageSource messageSource) {
        this.clienteService = clienteService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public String createCliente(@Valid ClienteDTO clienteDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ClienteDTO> clientes = clienteService.getAllClientes(pageable);
            model.addAttribute("clientes", clientes.getContent());
            model.addAttribute("clienteDTO", clienteDTO);
            model.addAttribute("page", clientes);
            return "main?table=tableClientes";
        }
        clienteService.createCliente(clienteDTO);
        return "redirect:/main?table=tableClientes";
    }

    @PostMapping("/update")
    public String updateCliente(@Valid ClienteDTO clienteDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            Page<ClienteDTO> clientes = clienteService.getAllClientes(pageable);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("clienteDTO", clienteDTO);
            model.addAttribute("clientes", clienteService.getAllClientes(pageable).getContent());
            model.addAttribute("page", clientes);
            return "main?table=tableClientes"; // Asegúrate de renderizar con datos correctos
        }
        clienteService.updateCliente(clienteDTO);
        return "redirect:/main?table=tableClientes";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        logger.debug("Llamando al método deleteCliente con id: {}", id);
        boolean isRemoved = clienteService.deleteCliente(id);
        if (!isRemoved) {
            logger.warn("Cliente con id: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Cliente con id: {} eliminado correctamente", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        ClienteDTO clienteDTO = clienteService.getCliente(id);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }
}