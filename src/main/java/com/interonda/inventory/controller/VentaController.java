package com.interonda.inventory.controller;

import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.dto.DepositoDTO;
import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.service.VentaService;
import com.interonda.inventory.service.DepositoService;
import com.interonda.inventory.service.ProductoService;
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

import java.math.BigDecimal;
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
    private final DepositoService depositoService;

    @Autowired
    public VentaController(ProductoService productoService, VentaService ventaService, ClienteService clienteService, MessageSource messageSource, DepositoService depositoService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.messageSource = messageSource;
        this.depositoService = depositoService;
    }

    @PostMapping
    public String createVenta(@Valid VentaDTO ventaDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        for (int i = 0; i < ventaDTO.getDetallesVenta().size(); i++) {
            DetalleVentaDTO detalle = ventaDTO.getDetallesVenta().get(i);
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                bindingResult.rejectValue("detallesVenta[" + i + "].cantidad", "error.detalle", "La cantidad debe ser un número positivo");
            }
            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                bindingResult.rejectValue("detallesVenta[" + i + "].precioUnitario", "error.detalle", "El precio unitario debe ser mayor que 0");
            }
            if (detalle.getDepositoId() == null) {
                bindingResult.rejectValue("detallesVenta[" + i + "].depositoId", "error.detalle", "El depósito no puede ser nulo");
            }
        }

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            Page<DepositoDTO> depositos = depositoService.getAllDepositos(pageable);
            model.addAttribute("ventaFormattedTotal", ventaService.formatTotal(ventaDTO.getTotal()));
            model.addAttribute("ventas", ventaService.getAllVentas(pageable).getContent());
            model.addAttribute("ventaDTO", ventaDTO);
            model.addAttribute("productos", productos);
            model.addAttribute("depositos", depositos.getContent());
            return "tableVentas";
        }

        ventaService.createVenta(ventaDTO);
        return "redirect:/tableVentas";
    }

    @PostMapping("/update")
    public String updateVenta(@Valid VentaDTO ventaDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        for (int i = 0; i < ventaDTO.getDetallesVenta().size(); i++) {
            DetalleVentaDTO detalle = ventaDTO.getDetallesVenta().get(i);
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                bindingResult.rejectValue("detallesVenta[" + i + "].cantidad", "error.detalle", "La cantidad debe ser un número positivo");
            }
            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                bindingResult.rejectValue("detallesVenta[" + i + "].precioUnitario", "error.detalle", "El precio unitario debe ser mayor que 0");
            }
            if (detalle.getDepositoId() == null) {
                bindingResult.rejectValue("detallesVenta[" + i + "].depositoId", "error.detalle", "El depósito no puede ser nulo");
            }
        }

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            Page<DepositoDTO> depositos = depositoService.getAllDepositos(pageable);
            model.addAttribute("ventaFormattedTotal", ventaService.formatTotal(ventaDTO.getTotal()));
            model.addAttribute("ventas", ventaService.getAllVentas(pageable).getContent());
            model.addAttribute("ventaDTO", ventaDTO);
            model.addAttribute("productos", productos);
            model.addAttribute("depositos", depositos.getContent());
            return "tableVentas";
        }

        ventaService.updateVenta(ventaDTO);
        return "redirect:/tableVentas";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        logger.debug("Llamando al método deleteVenta con id: {}", id);
        boolean isRemoved = ventaService.deleteVenta(id);
        if (!isRemoved) {
            logger.warn("Venta con id: {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Venta con id: {} eliminada correctamente", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> getVentaById(@PathVariable Long id) {
        VentaDTO ventaDTO = ventaService.getVenta(id);
        ventaDTO.getDetallesVenta().forEach(detalle -> {
            detalle.setPrecioUnitarioString(ventaService.formatPrecioUnitario(detalle.getPrecioUnitario())); // Formatear el precio unitario
            detalle.setTotalDetalleFormatted(ventaService.formatTotal(detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad())))); // Formatear el total
        });
        return new ResponseEntity<>(ventaDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showVentas(@RequestParam(required = false) String fecha, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("id").descending());
        Page<VentaDTO> ventas;
        if (fecha != null && !fecha.isEmpty()) {
            ventas = ventaService.searchVentasByFecha(LocalDate.parse(fecha), newPageable);
        } else {
            ventas = ventaService.getAllVentas(newPageable);
        }
        model.addAttribute("ventas", ventas.getContent());
        model.addAttribute("ventaDTO", new VentaDTO());
        model.addAttribute("page", ventas);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable clientesPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        model.addAttribute("clientes", clienteService.getAllClientes(clientesPageable).getContent());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        model.addAttribute("depositos", depositoService.obtenerTodosLosDepositos());
        model.addAttribute("currentPage", "tableVentas");
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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable clientesPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        model.addAttribute("clientes", clienteService.getAllClientes(clientesPageable).getContent());
        return "tableVentas";
    }
}