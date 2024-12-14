package com.interonda.inventory.controller;

import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.service.ClienteService;
import com.interonda.inventory.service.ProductoService;
import com.interonda.inventory.service.VentaService;
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

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tableVentas")
public class VentaController {

    private static final Logger logger = LoggerFactory.getLogger(VentaController.class);

    private final ProductoService productoService;
    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final MessageSource messageSource;

    @Autowired
    public VentaController(ProductoService productoService, VentaService ventaService, ClienteService clienteService, MessageSource messageSource) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public String createVenta(@Valid VentaDTO ventaDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("ventas", ventaService.getAllVentas(pageable).getContent());
            model.addAttribute("ventaDTO", ventaDTO);
            return "tableVentas";
        }
        ventaService.createVenta(ventaDTO);
        return "redirect:/tableVentas";
    }

    @PostMapping("/update")
    public String updateVenta(@Valid VentaDTO ventaDTO, BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("ventaDTO", ventaDTO);
            model.addAttribute("ventas", ventaService.getAllVentas(pageable).getContent());
            model.addAttribute("clientes", clienteService.getAllClientes(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            return "tableVentas";
        }
        VentaDTO updatedVentaDTO = ventaService.updateVenta(ventaDTO);
        model.addAttribute("ventaDTO", updatedVentaDTO);
        return "redirect:/tableVentas";
    }

    @PostMapping("/{id}")
    public String deleteVenta(@PathVariable Long id) {
        logger.debug("Llamando al método deleteVenta con id: {}", id);
        ventaService.deleteVenta(id);
        logger.debug("Venta con id: {} eliminada correctamente", id);
        return "redirect:/tableVentas";
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> getVentaById(@PathVariable Long id) {
        VentaDTO ventaDTO = ventaService.getVenta(id);
        return new ResponseEntity<>(ventaDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showVentas(@RequestParam(required = false) String fecha, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        Page<VentaDTO> ventas;
        if (fecha != null && !fecha.isEmpty()) {
            logger.info("Solicitud recibida para buscar ventas por fecha: {}", fecha);
            ventas = ventaService.searchVentasByFecha(LocalDate.parse(fecha), newPageable);
        } else {
            ventas = ventaService.getAllVentas(newPageable);
        }
        model.addAttribute("ventas", ventas.getContent());
        model.addAttribute("ventaDTO", new VentaDTO());
        model.addAttribute("page", ventas);
        model.addAttribute("clientes", clienteService.getAllClientes(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "tableVentas";
    }

    @GetMapping("/search")
    public String searchVentas(@RequestParam(required = false) String nombreCliente, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        Page<VentaDTO> ventas;

        if (nombreCliente != null && !nombreCliente.isEmpty()) {
            logger.info("Solicitud recibida para buscar ventas por nombre de cliente: {}", nombreCliente);
            ventas = ventaService.searchVentasByClienteNombre(nombreCliente, newPageable);
        } else {
            ventas = ventaService.getAllVentas(newPageable);
        }

        model.addAttribute("ventas", ventas.getContent());
        model.addAttribute("ventaDTO", new VentaDTO());
        model.addAttribute("page", ventas);
        model.addAttribute("clientes", clienteService.getAllClientes(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        return "tableVentas";
    }
}