package com.interonda.inventory.controller;

import com.interonda.inventory.dto.CategoriaDTO;
import com.interonda.inventory.entity.PageDetails;
import com.interonda.inventory.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/main")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final PageService pageService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;
    private final DepositoService depositoService;
    private final CompraService compraService;
    private final VentaService ventaService;
    private final UsuarioService usuarioService;
    private final PresupuestarService presupuestarService;
    private final CategoriaService categoriaService;
    private final ClienteService clienteService;

    @Autowired
    public MainController(PageService pageService, ProveedorService proveedorService, ProductoService productoService, DepositoService depositoService, CompraService compraService, VentaService ventaService, UsuarioService usuarioService, PresupuestarService presupuestarService, CategoriaService categoriaService, ClienteService clienteService) {
        this.pageService = pageService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
        this.depositoService = depositoService;
        this.compraService = compraService;
        this.ventaService = ventaService;
        this.usuarioService = usuarioService;
        this.presupuestarService = presupuestarService;
        this.categoriaService = categoriaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String getMainTable(@RequestParam(name = "table", required = false) String table, @RequestParam(name = "page", required = false, defaultValue = "0") int page, Model model) {
        logger.debug("getMainTable called with table: {} and page: {}", table, page);
        String tableName = table.replace("table", ""); // Eliminar el prefijo "table"
        Page<Map<String, Object>> tablePage = getPageForTable(table, page);
        PageDetails pageDetails = pageService.getPageDetails(tablePage);

        model.addAttribute("title", "Página Principal");
        model.addAttribute("headers", getHeadersForTable(table));
        model.addAttribute("rows", tablePage.getContent());
        model.addAttribute("pageDetails", pageDetails);
        model.addAttribute("table", tableName); // Pasar el nombre de la tabla sin el prefijo

        if ("Categorias".equals(tableName)) {
            model.addAttribute("categoriaDTO", new CategoriaDTO());
        }

        return "layout";
    }

    private Page<Map<String, Object>> getPageForTable(String table, int page) {
        logger.debug("getPageForTable called with table: {} and page: {}", table, page);
        switch (table) {
            case "tableProveedores":
                return proveedorService.getAllProveedoresAsMap(PageRequest.of(page, 10));
            case "tableProductos":
                return productoService.getAllProductosAsMap(PageRequest.of(page, 10));
            case "tableDepositos":
                return depositoService.getAllDepositosAsMap(PageRequest.of(page, 10));
            case "tableCompras":
                return compraService.getAllComprasAsMap(PageRequest.of(page, 10));
            case "tableVentas":
                return ventaService.getAllVentasAsMap(PageRequest.of(page, 10));
            case "tableUsuarios":
                return usuarioService.getAllUsuariosAsMap(PageRequest.of(page, 10));
            case "tablePresupuestar":
                return presupuestarService.getAllPresupuestarAsMap(PageRequest.of(page, 10));
            case "tableCategorias":
                return categoriaService.getAllCategoriaAsMap(PageRequest.of(page, 10));
            case "tableClientes":
                return clienteService.getAllClientesAsMap(PageRequest.of(page, 10));
            default:
                throw new IllegalArgumentException("Tabla no reconocida: " + table);
        }
    }

    private List<String> getHeadersForTable(String table) {
        logger.debug("getHeadersForTable called with table: {}", table);
        switch (table) {
            case "tableProveedores":
                return List.of("ID", "Nombre", "Contacto", "Dirección", "Pais", "Email");
            case "tableProductos":
                return List.of("ID", "Nombre", "Descripción", "Precio", "Costo", "Categoría");
            case "tableDepositos":
                return List.of("ID", "Nombre", "Provincia", "Dirección", "Contacto");
            case "tableCompras":
                return List.of("ID", "Proveedor", "Fecha", "Estado", "Método de Pago", "Impuestos", "Total");
            case "tableVentas", "tablePresupuestar":
                return List.of("ID", "Cliente", "Fecha", "Estado", "Método de Pago", "Impuestos", "Total");
            case "tableUsuarios":
                return List.of("ID", "Nombre", "Contraseña", "Contacto", "Rol");
            case "tableCategorias":
                return List.of("ID", "Nombre", "Descripcion");
            case "tableClientes":
                return List.of("ID", "Nombre", "Contacto", "Dirección", "País", "Ciudad", "Email");
            default:
                throw new IllegalArgumentException("Tabla no reconocida: " + table);
        }
    }
}