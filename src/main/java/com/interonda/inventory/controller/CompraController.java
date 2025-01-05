package com.interonda.inventory.controller;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.DepositoDTO;
import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.service.*;
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
@RequestMapping("/tableCompras")
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    private final ProductoService productoService;

    private final CompraService compraService;

    private final ProveedorService proveedorService;

    private final MessageSource messageSource;

    private final DepositoService depositoService;

    @Autowired
    public CompraController(ProductoService productoService, CompraService compraService, ProveedorService proveedorService, MessageSource messageSource, DepositoService depositoService) {
        this.productoService = productoService;
        this.compraService = compraService;
        this.proveedorService = proveedorService;
        this.messageSource = messageSource;
        this.depositoService = depositoService;
    }

    @PostMapping
    public String createCompra(@Valid CompraDTO compraDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        for (int i = 0; i < compraDTO.getDetallesCompra().size(); i++) {
            DetalleCompraDTO detalle = compraDTO.getDetallesCompra().get(i);
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                bindingResult.rejectValue("detallesCompra[" + i + "].cantidad", "error.detalle", "La cantidad debe ser un número positivo");
            }
            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                bindingResult.rejectValue("detallesCompra[" + i + "].precioUnitario", "error.detalle", "El precio unitario debe ser mayor que 0");
            }
            if (detalle.getDepositoId() == null) {
                bindingResult.rejectValue("detallesCompra[" + i + "].depositoId", "error.detalle", "El depósito no puede ser nulo");
            }
        }

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            Page<DepositoDTO> depositos = depositoService.getAllDepositos(pageable);
            model.addAttribute("compraFormattedTotal", compraService.formatTotal(compraDTO.getTotal()));
            model.addAttribute("compras", compraService.getAllCompras(pageable).getContent());
            model.addAttribute("compraDTO", compraDTO);
            model.addAttribute("page", productos);
            model.addAttribute("page", depositos);
            return "tableCompras";
        }

        compraService.createCompra(compraDTO);
        return "redirect:/tableCompras";
    }

    @PostMapping("/update")
    public String updateCompra(@Valid CompraDTO compraDTO, BindingResult bindingResult, Model model, Pageable pageable) {
        for (int i = 0; i < compraDTO.getDetallesCompra().size(); i++) {
            DetalleCompraDTO detalle = compraDTO.getDetallesCompra().get(i);
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                bindingResult.rejectValue("detallesCompra[" + i + "].cantidad", "error.detalle", "La cantidad debe ser un número positivo");
            }
            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                bindingResult.rejectValue("detallesCompra[" + i + "].precioUnitario", "error.detalle", "El precio unitario debe ser mayor que 0");
            }
            if (detalle.getDepositoId() == null) {
                bindingResult.rejectValue("detallesCompra[" + i + "].depositoId", "error.detalle", "El depósito no puede ser nulo");
            }
        }

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).collect(Collectors.joining("<br>"));
            model.addAttribute("errorMessage", errorMessage);
            Page<ProductoDTO> productos = productoService.getAllProductos(pageable);
            Page<DepositoDTO> depositos = depositoService.getAllDepositos(pageable);
            model.addAttribute("compraFormattedTotal", compraService.formatTotal(compraDTO.getTotal()));
            model.addAttribute("compras", compraService.getAllCompras(pageable).getContent());
            model.addAttribute("compraDTO", compraDTO);
            model.addAttribute("productos", productos);
            model.addAttribute("depositos", depositos.getContent());
            return "tableCompras";
        }

        compraService.updateCompra(compraDTO);
        return "redirect:/tableCompras";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompra(@PathVariable Long id) {
        logger.debug("Llamando al método deleteCompra con id: {}", id);
        boolean isRemoved = compraService.deleteCompra(id);
        if (!isRemoved) {
            logger.warn("Compra con id: {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Compra con id: {} eliminada correctamente", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> getCompraById(@PathVariable Long id) {
        CompraDTO compraDTO = compraService.getCompra(id);
        compraDTO.getDetallesCompra().forEach(detalle -> {
            detalle.setPrecioUnitarioString(compraService.formatPrecioUnitario(detalle.getPrecioUnitario())); // Formatear el precio unitario
            detalle.setTotalFormatted(compraService.formatTotal(detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad())))); // Formatear el total
        });
        return new ResponseEntity<>(compraDTO, HttpStatus.OK);
    }

    @GetMapping
    public String showCompras(@RequestParam(required = false) String fecha, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("id").descending());
        Page<CompraDTO> compras;
        if (fecha != null && !fecha.isEmpty()) {
            compras = compraService.searchComprasByFecha(LocalDate.parse(fecha), newPageable);
        } else {
            compras = compraService.getAllCompras(newPageable);
        }
        model.addAttribute("compras", compras.getContent());
        model.addAttribute("compraDTO", new CompraDTO());
        model.addAttribute("page", compras);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable proveedoresPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        model.addAttribute("proveedores", proveedorService.getAllProveedores(proveedoresPageable, sort).getContent());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        model.addAttribute("depositos", depositoService.obtenerTodosLosDepositos());
        model.addAttribute("currentPage", "tableCompras");
        return "tableCompras";
    }

    @GetMapping("/search")
    public String searchCompras(@RequestParam(required = false) String nombreProveedor, Model model, Pageable pageable) {
        int pageSize = 15;
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        Page<CompraDTO> compras;

        if (nombreProveedor != null && !nombreProveedor.isEmpty()) {
            logger.info("Solicitud recibida para buscar compras por nombre de proveedor: {}", nombreProveedor);
            compras = compraService.searchComprasByProveedorNombre(nombreProveedor, newPageable);
        } else {
            compras = compraService.getAllCompras(newPageable);
        }


        model.addAttribute("compras", compras.getContent());
        model.addAttribute("compraDTO", new CompraDTO());
        model.addAttribute("page", compras);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable proveedoresPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        model.addAttribute("proveedores", proveedorService.getAllProveedores(proveedoresPageable, sort).getContent());
        return "tableCompras";
    }
}
