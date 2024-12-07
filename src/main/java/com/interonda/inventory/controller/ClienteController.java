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
import org.springframework.data.domain.Pageable;
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
    public String createCliente(@Valid ClienteDTO clienteDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("clientes", clienteService.getAllClientes(pageable).getContent());
            model.addAttribute("clienteDTO", clienteDTO);
            return "tableClientes";
        }
        clienteService.createCliente(clienteDTO);
        return "redirect:/tableClientes";
    }

    @PostMapping("/update")
    public String updateCliente(@Valid ClienteDTO clienteDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.getAllClientes(pageable).getContent());
            model.addAttribute("clienteDTO", clienteDTO);
            return "tableClientes";
        }
        clienteService.updateCliente(clienteDTO);
        return "redirect:/tableClientes";
    }

    @PostMapping("/{id}")
    public String deleteCliente(@PathVariable Long id) {
        logger.debug("Llamando al método deleteCliente con id: {}", id);
        clienteService.deleteCliente(id);
        logger.debug("Cliente con id: {} eliminado correctamente", id);
        return "redirect:/tableClientes";  // Redirige después de la eliminación
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        ClienteDTO clienteDTO = clienteService.getCliente(id);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showClientes(@RequestParam(required = false) String name, Model model, Pageable pageable) {
        Page<ClienteDTO> clientes;
        if (name != null && !name.isEmpty()) {
            logger.info("Solicitud recibida para buscar clientes por nombre: {}", name);
            clientes = clienteService.searchClientesByName(name, pageable);
        } else {
            clientes = clienteService.getAllClientes(pageable);
        }
        model.addAttribute("clientes", clientes.getContent());
        model.addAttribute("clienteDTO", new ClienteDTO());
        return "tableClientes";
    }

    @GetMapping("/search")
    public String searchClientesByName(@RequestParam String name, Model model, Pageable pageable) {
        logger.info("Solicitud recibida para buscar clientes por nombre: {}", name);
        Page<ClienteDTO> clientes = clienteService.searchClientesByName(name, pageable);
        model.addAttribute("clientes", clientes.getContent());
        model.addAttribute("clienteDTO", new ClienteDTO());
        return "tableClientes";
    }

}