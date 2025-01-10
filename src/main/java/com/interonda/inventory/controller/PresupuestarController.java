 /*
package com.interonda.inventory.controller;

import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.service.ClienteService;
import com.interonda.inventory.service.PresupuestarService;
import com.interonda.inventory.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/tablePresupuestar")
public class PresupuestarController {
    private static final Logger logger = LoggerFactory.getLogger(VentaController.class);

    private final ProductoService productoService;
    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final MessageSource messageSource;
    private final PresupuestarService presupuestarService;

    @Autowired
    public PresupuestarController(ProductoService productoService, VentaService ventaService, ClienteService clienteService, MessageSource messageSource, PresupuestarService presupuestarService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.messageSource = messageSource;
        this.presupuestarService = presupuestarService;
    }

    @GetMapping("/generatePdf/{id}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        try {
            byte[] pdfContent = presupuestarService.generatePdf(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "presupuestar.pdf");
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error generando PDF", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> getVentaById(@PathVariable Long id) {
        VentaDTO ventaDTO = ventaService.getVenta(id);
        return new ResponseEntity<>(ventaDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showVentas(@RequestParam(required = false) String fecha, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("id").descending());
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
        model.addAttribute("currentPage", "tableVentas");
        return "tablePresupuestar";
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
} */
