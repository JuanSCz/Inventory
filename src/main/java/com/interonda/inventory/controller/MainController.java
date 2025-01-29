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
    private final UsuarioService usuarioService;
    private final PresupuestarService presupuestarService;
    private final CategoriaService categoriaService;
    private final ClienteService clienteService;
    private final DepositosProductosService depositosProductosService;
    private final CompraService compraService;
    private final VentaService ventaService;

    @Autowired
    public MainController(PageService pageService, ProveedorService proveedorService, ProductoService productoService, DepositoService depositoService, UsuarioService usuarioService, PresupuestarService presupuestarService, CategoriaService categoriaService, ClienteService clienteService, DepositosProductosService depositosProductosService, CompraService compraService, VentaService ventaService) {
        this.pageService = pageService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
        this.depositoService = depositoService;
        this.usuarioService = usuarioService;
        this.presupuestarService = presupuestarService;
        this.categoriaService = categoriaService;
        this.clienteService = clienteService;
        this.depositosProductosService = depositosProductosService;
        this.compraService = compraService;
        this.ventaService = ventaService;
    }

    @GetMapping
    public String getMainTable(@RequestParam(name = "table", required = false) String table, @RequestParam(name = "page", required = false, defaultValue = "0") int page, @RequestParam(name = "depositoId", required = false) Long depositoId, Model model) {
        logger.debug("getMainTable called with table: {}, page: {}, depositoId: {}", table, page, depositoId);
        String tableName = table.replace("table", ""); // Eliminar el prefijo "table"

        Page<Map<String, Object>> tablePage = getPageForTable(table, page, depositoId);
        PageDetails pageDetails = pageService.getPageDetails(tablePage);

        model.addAttribute("title", "Página Principal");
        model.addAttribute("headers", getHeadersForTable(table));
        model.addAttribute("rows", tablePage.getContent());
        model.addAttribute("pageDetails", pageDetails);
        model.addAttribute("table", tableName); // Pasar el nombre de la tabla sin el prefijo

        if ("Categorias".equals(tableName)) {
            model.addAttribute("categoriaDTO", new CategoriaDTO());
        }

        if ("Clientes".equals(tableName)) {
            model.addAttribute("cliente", new Cliente());
            model.addAttribute("clienteDTO", new ClienteDTO());
        }

        if ("Proveedores".equals(tableName)) {
            model.addAttribute("proveedor", new Proveedor());
            model.addAttribute("proveedorDTO", new ProveedorDTO());
        }

        if ("Depositos".equals(tableName)) {
            model.addAttribute("deposito", new Deposito());
            model.addAttribute("depositoDTO", new DepositoDTO());
        }

        if ("Productos".equals(tableName)) {
            model.addAttribute("producto", new Producto());
            model.addAttribute("productoDTO", new ProductoDTO());
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("DepositosProductos".equals(tableName)) {
            model.addAttribute("producto", new Producto());
            model.addAttribute("productoDTO", new ProductoDTO());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("selectedDepositoId", depositoId); // Añadir el depósito seleccionado al modelo
        }
        return "layout";
    }

    private Page<Map<String, Object>> getPageForTable(String table, int page, Long depositoId) {
        logger.debug("getPageForTable called with table: {}, page: {}, depositoId: {}", table, page, depositoId);
        switch (table) {
            case "tableProveedores":
                return proveedorService.getAllProveedoresAsMap(PageRequest.of(page, 10));
            case "tableProductos":
                return productoService.getAllProductosAsMap(PageRequest.of(page, 10));
            case "tableDepositos":
                return depositoService.getAllDepositosAsMap(PageRequest.of(page, 10));
            case "tableUsuarios":
                return usuarioService.getAllUsuariosAsMap(PageRequest.of(page, 10));
            case "tablePresupuestar":
                return presupuestarService.getAllPresupuestarAsMap(PageRequest.of(page, 10));
            case "tableCategorias":
                return categoriaService.getAllCategoriaAsMap(PageRequest.of(page, 10));
            case "tableClientes":
                return clienteService.getAllClientesAsMap(PageRequest.of(page, 10));
            case "tableDepositosProductos":
                return depositosProductosService.getProductosByDepositoAsMap(depositoId, PageRequest.of(page, 10));
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
            case "tableDepositosProductos":
                return List.of("ID", "Nombre", "Descripcion", "Stock", "MAC Address", "Numero de Serie", "Código de Barras", "Categoria");
            default:
                throw new IllegalArgumentException("Tabla no reconocida: " + table);
        }
    }
}