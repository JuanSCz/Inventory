package com.interonda.inventory.controller;

import com.interonda.inventory.dto.*;
import com.interonda.inventory.entity.*;
import com.interonda.inventory.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final CategoriaService categoriaService;
    private final ClienteService clienteService;
    private final VentaService ventaService;

    @Autowired
    public MainController(PageService pageService, ProveedorService proveedorService, ProductoService productoService, DepositoService depositoService, CategoriaService categoriaService, ClienteService clienteService, VentaService ventaService) {
        this.pageService = pageService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
        this.depositoService = depositoService;
        this.categoriaService = categoriaService;
        this.clienteService = clienteService;
        this.ventaService = ventaService;

    }

    @GetMapping
    public String getMainTable(@RequestParam(name = "table", required = false) String table, @RequestParam(name = "page", required = false, defaultValue = "0") int page, @RequestParam(name = "depositoId", required = false) Long depositoId, Model model) {
        if (table == null) {
            model.addAttribute("errorMessage", "El parámetro 'table' es obligatorio.");
            return "errorPage";
        }

        logger.debug("getMainTable called with table: {}, page: {}, depositoId: {}", table, page, depositoId);
        String tableName = table.replace("table", ""); // Eliminar el prefijo "table"

        Page<Map<String, Object>> tablePage = getPageForTable(table, page, depositoId);
        PageDetails pageDetails = pageService.getPageDetails(tablePage);

        model.addAttribute("title", "Página Principal");
        model.addAttribute("headers", getHeadersForTable(table));
        model.addAttribute("rows", tablePage.getContent());
        model.addAttribute("pageDetails", pageDetails);
        model.addAttribute("table", tableName); // Pasar el nombre de la tabla sin el prefijo

        if ("Proveedores".equals(tableName)) {
            model.addAttribute("proveedor", new Proveedor());
            model.addAttribute("proveedorDTO", new ProveedorDTO());
        }

        if ("Productos".equals(tableName)) {
            model.addAttribute("producto", new Producto());
            model.addAttribute("productoDTO", new ProductoDTO());
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Depositos".equals(tableName)) {
            model.addAttribute("deposito", new Deposito());
            model.addAttribute("depositoDTO", new DepositoDTO());
        }

        if ("Categorias".equals(tableName)) {
            model.addAttribute("categoriaDTO", new CategoriaDTO());
        }

        if ("Clientes".equals(tableName)) {
            model.addAttribute("cliente", new Cliente());
            model.addAttribute("clienteDTO", new ClienteDTO());
        }

        if ("Clientes".equals(tableName)) {
            model.addAttribute("cliente", new Cliente());
            model.addAttribute("clienteDTO", new ClienteDTO());
        }

        if ("Ventas".equals(tableName)) {
            model.addAttribute("venta", new Venta());
            model.addAttribute("ventaDTO", new VentaDTO());
            model.addAttribute("clientes", clienteService.getAllClientes(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            model.addAttribute("depositos", depositoService.obtenerTodosLosDepositos());
        }

        return "layout";

    }

    private Page<Map<String, Object>> getPageForTable(String table, int page, Long depositoId) {
        switch (table) {
            case "tableProveedores":
                return proveedorService.getAllProveedoresAsMap(PageRequest.of(page, 10));
            case "tableProductos":
                return productoService.getAllProductosAsMap(PageRequest.of(page, 10));
            case "tableDepositos":
                return depositoService.getAllDepositosAsMap(PageRequest.of(page, 10));
            case "tableCategorias":
                return categoriaService.getAllCategoriaAsMap(PageRequest.of(page, 10));
            case "tableClientes":
                return clienteService.getAllClientesAsMap(PageRequest.of(page, 10));
            case "tableVentas":
                return ventaService.getAllVentasAsMap(PageRequest.of(page, 10));
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
                return List.of("ID", "Nombre", "Ubicación", "Capacidad");
            case "tableCategorias":
                return List.of("ID", "Nombre", "Descripción");
            case "tableClientes":
                return List.of("ID", "Nombre", "Email", "Teléfono");
            case "tableVentas":
                return List.of("ID", "Cliente", "Fecha", "Estado", "Método de Pago", "Impuestos", "Total");
            default:
                throw new IllegalArgumentException("Tabla no reconocida: " + table);
        }
    }
}