package com.interonda.inventory.controller;

import com.interonda.inventory.dto.*;
import com.interonda.inventory.entity.*;
import com.interonda.inventory.repository.CompraRepository;
import com.interonda.inventory.repository.ProductoRepository;
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
    private final CategoriaService categoriaService;
    private final ClienteService clienteService;
    private final VentaService ventaService;
    private final CompraService compraService;
    private final HistorialStockService historialStockService;
    private final PresupuestarService presupuestarService;
    private final ProductoRepository productoRepository;
    private final CompraRepository compraRepository;
    private final DepositoProductoService depositoProductoService;
    private final DepositosProductosUnidadService depositosProductosUnidadService;

    @Autowired
    public MainController(PageService pageService, ProveedorService proveedorService, ProductoService productoService, DepositoService depositoService, CategoriaService categoriaService, ClienteService clienteService, VentaService ventaService, CompraService compraService, HistorialStockService historialStockService, PresupuestarService presupuestarService, ProductoRepository productoRepository, CompraRepository compraRepository, DepositoProductoService depositoProductoService, DepositosProductosUnidadService depositosProductosUnidadService) {
        this.pageService = pageService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
        this.depositoService = depositoService;
        this.categoriaService = categoriaService;
        this.clienteService = clienteService;
        this.ventaService = ventaService;
        this.compraService = compraService;
        this.historialStockService = historialStockService;
        this.presupuestarService = presupuestarService;
        this.productoRepository = productoRepository;
        this.compraRepository = compraRepository;
        this.depositoProductoService = depositoProductoService;
        this.depositosProductosUnidadService = depositosProductosUnidadService;
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
        model.addAttribute("table", tableName); // Asegúrate de pasar el nombre de la tabla sin el prefijo "table"

        if ("Categorias".equals(tableName)) {
            model.addAttribute("categoriaDTO", new CategoriaDTO());
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Clientes".equals(tableName)) {
            model.addAttribute("cliente", clienteService.getAllClientes(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("clienteDTO", new ClienteDTO());
        }

        if ("Compras".equals(tableName)) {
            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            Pageable proveedoresPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            model.addAttribute("compra", new Compra());
            model.addAttribute("compraDTO", new CompraDTO());
            model.addAttribute("proveedores", proveedorService.getAllProveedores(proveedoresPageable, sort).getContent());
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            model.addAttribute("depositos", depositoService.obtenerTodosLosDepositos());
        }

        if ("Depositos".equals(tableName)) {
            model.addAttribute("deposito", new Deposito());
            model.addAttribute("depositoDTO", new DepositoDTO());
        }

        if ("HistorialStock".equals(tableName)) {
            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            Pageable historialStockPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            Page<HistorialStockDTO> historialStockPage = historialStockService.getAllHistorialStock(historialStockPageable);
            model.addAttribute("historiales", historialStockPage.getContent());
            model.addAttribute("page", historialStockPage);
        }

        if ("Productos".equals(tableName)) {
            model.addAttribute("producto", new Producto());
            model.addAttribute("productoDTO", new ProductoDTO());
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Proveedores".equals(tableName)) {
            Pageable proveedoresPageable = PageRequest.of(page, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id"));
            model.addAttribute("proveedorDTO", new ProveedorDTO());
            model.addAttribute("proveedor", proveedorService.getAllProveedores(proveedoresPageable, Sort.by(Sort.Direction.ASC, "id")).getContent());
        }

        if ("Ventas".equals(tableName)) {
            model.addAttribute("venta", new Venta());
            model.addAttribute("ventaDTO", new VentaDTO());
            model.addAttribute("clientes", clienteService.getAllClientes(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            model.addAttribute("depositos", depositoService.obtenerTodosLosDepositos());
        }

        if ("Presupuestar".equals(tableName)) {
            model.addAttribute("venta", new Venta());
            model.addAttribute("ventaDTO", new VentaDTO());
            model.addAttribute("clientes", clienteService.getAllClientes(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            model.addAttribute("depositos", depositoService.obtenerTodosLosDepositos());
        }

        if ("DepositoProducto".equals(tableName)) {
            if (depositoId == null) {
                depositoId = depositoProductoService.getDefaultDepositoId();
                return "redirect:/main?table=tableDepositoProducto&depositoId=" + depositoId;
            }
            model.addAttribute("producto", new Producto());
            model.addAttribute("productoDTO", new ProductoDTO());
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("selectedDepositoId", depositoId); // Añadir el depósito seleccionado al modelo
            model.addAttribute("productos", depositoProductoService.getProductosByDeposito(depositoId, PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        logger.debug("Final model attributes: {}", model.asMap());
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
            case "tableCompras":
                return compraService.getAllComprasAsMap(PageRequest.of(page, 10));
            case "tableHistorialStock":
                return historialStockService.getAllHistorialStockAsMap(PageRequest.of(page, 10));
            case "tablePresupuestar":
                return presupuestarService.getAllPresupuestarAsMap(PageRequest.of(page, 10));
            case "tableDepositoProducto":
                return depositoProductoService.getProductosByDepositoAsMap(depositoId, PageRequest.of(page, 10));
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
            case "tableCategorias":
                return List.of("ID", "Nombre", "Descripción");
            case "tableClientes":
                return List.of("ID", "Nombre", "Email", "Teléfono");
            case "tableVentas", "tablePresupuestar":
                return List.of("ID", "Cliente", "Fecha", "Estado", "Método de Pago", "Impuestos", "Total");
            case "tableCompras":
                return List.of("ID", "proveedor", "Fecha", "Estado", "Método de Pago", "Impuestos", "Total");
            case "tableHistorialStock":
                return List.of("ID", "Cant Anterior", "Cant Nueva", "Actualización", "Motivo", "Movimiento", "Producto", "Depósito", "Usuario");
            case "tableDepositoProducto":
                return List.of("ID", "Nombre", "Stock", "Mac Address", "Numero De Serie", "Codigo De Barras", "Categoria");
            default:
                throw new IllegalArgumentException("Tabla no reconocida: " + table);
        }
    }

    @GetMapping("/search")
    public String searchTable(@RequestParam(name = "table") String table, @RequestParam(name = "name") String name, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "depositoId", required = false) Long depositoId, Model model) {
        if (table == null || name == null) {
            model.addAttribute("errorMessage", "Los parámetros 'table' y 'name' son obligatorios.");
            return "errorPage";
        }

        logger.debug("searchTable called with table: {}, name: {}, page: {}, depositoId: {}", table, name, page, depositoId);
        String tableName = "table" + table; // Agregar el prefijo "table"

        Page<Map<String, Object>> tablePage = searchPageForTable(tableName, name, page, depositoId);
        PageDetails pageDetails = pageService.getPageDetails(tablePage);

        model.addAttribute("title", "Página Principal");
        model.addAttribute("headers", getHeadersForTable(tableName));
        model.addAttribute("rows", tablePage.getContent());
        model.addAttribute("pageDetails", pageDetails);
        model.addAttribute("table", table);

        if ("Categorias".equals(table)) {
            model.addAttribute("categoriaDTO", new CategoriaDTO());
            model.addAttribute("categorias", categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Clientes".equals(table)) {
            model.addAttribute("clienteDTO", new ClienteDTO());
            model.addAttribute("cliente", clienteService.getAllClientes(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Depositos".equals(table)) {
            model.addAttribute("depositoDTO", new DepositoDTO());
            model.addAttribute("deposito", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Productos".equals(table)) {
            model.addAttribute("productoDTO", new ProductoDTO());
            model.addAttribute("producto", productoService.getAllProductos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Proveedores".equals(table)) {
            Pageable proveedoresPageable = PageRequest.of(page, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "id"));
            model.addAttribute("proveedorDTO", new ProveedorDTO());
            model.addAttribute("proveedor", proveedorService.getAllProveedores(proveedoresPageable, Sort.by(Sort.Direction.DESC, "id")).getContent());
        }

        if ("Ventas".equals(table)) {
            model.addAttribute("ventaDTO", new VentaDTO());
            model.addAttribute("venta", ventaService.getAllVentas(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Compras".equals(table)) {
            model.addAttribute("compraDTO", new CompraDTO());
            model.addAttribute("compra", compraService.getAllCompras(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("Presupuestar".equals(table)) {
            model.addAttribute("ventaDTO", new VentaDTO());
            model.addAttribute("venta", ventaService.getAllVentas(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        }

        if ("DepositoProducto".equals(tableName)) {
            model.addAttribute("depositos", depositoService.getAllDepositos(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
            model.addAttribute("selectedDepositoId", depositoId);
        }

        logger.debug("Final model attributes: {}", model.asMap());
        return "layout";
    }

    private Page<Map<String, Object>> searchPageForTable(String table, String name, int page, Long depositoId) {
        switch (table) {
            case "tableCategorias":
                Page<CategoriaDTO> categoriaPage = categoriaService.searchCategoriasByName(name, PageRequest.of(page, 10));
                return categoriaPage.map(categoria -> Map.of("id", categoria.getId(), "nombre", categoria.getNombre(), "descripción", categoria.getDescripcion()));
            case "tableClientes":
                Page<ClienteDTO> clientePage = clienteService.searchClientesByName(name, PageRequest.of(page, 10));
                return clientePage.map(cliente -> Map.of("id", cliente.getId(), "nombre", cliente.getNombre(), "contacto", cliente.getContactoCliente(), "dirección", cliente.getDireccion(), "país", cliente.getPais(), "ciudad", cliente.getCiudad(), "email", cliente.geteMailCliente()));
            case "tableDepositos":
                Page<DepositoDTO> depositoPage = depositoService.searchDepositosByName(name, PageRequest.of(page, 10));
                return depositoPage.map(deposito -> Map.of("id", deposito.getId(), "nombre", deposito.getNombre(), "provincia", deposito.getProvincia(), "dirección", deposito.getDireccion(), "contacto", deposito.getContactoDeposito()));
            case "tableProductos":
                Page<Producto> productosPage = productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(name, PageRequest.of(page, 10));
                return productosPage.map(producto -> Map.of("id", producto.getId(), "nombre", producto.getNombre(), "descripción", producto.getDescripcion(), "precio", producto.getPrecio(), "costo", producto.getCosto(), "categoría", producto.getCategoria() != null ? producto.getCategoria().getNombre() : null));
            case "tableProveedores":
                Page<ProveedorDTO> proveedorPage = proveedorService.searchProveedoresByName(name, PageRequest.of(page, 10));
                return proveedorPage.map(proveedor -> Map.of("id", proveedor.getId(), "nombre", proveedor.getNombre(), "contacto", proveedor.getContactoProveedor(), "dirección", proveedor.getDireccion(), "pais", proveedor.getPais(), "email", proveedor.getEmailProveedor()));
            case "tableVentas":
                Page<VentaDTO> ventaPage = ventaService.searchVentasByClienteNombre(name, PageRequest.of(page, 10));
                return ventaPage.map(venta -> Map.of("id", venta.getId(), "cliente", venta.getCliente().getNombre(), "fecha", venta.getFecha(), "estado", venta.getEstado(), "método de pago", venta.getMetodoPago(), "impuestos", venta.getImpuestos(), "total", venta.getTotal()));
            case "tableCompras":
                Page<CompraDTO> compraPage = compraService.searchComprasByProveedorNombre(name, PageRequest.of(page, 10));
                return compraPage.map(compra -> Map.of("id", compra.getId(), "proveedor", compra.getProveedorNombre(), "fecha", compra.getFecha(), "estado", compra.getEstado(), "método de pago", compra.getMetodoPago(), "impuestos", compra.getImpuestos(), "total", compra.getTotal()));
            case "tablePresupuestar":
                Page<VentaDTO> presupuestarPage = presupuestarService.searchVentasByClienteNombre(name, PageRequest.of(page, 10));
                return presupuestarPage.map(venta -> Map.of("id", venta.getId(), "cliente", venta.getCliente().getNombre(), "fecha", venta.getFecha(), "estado", venta.getEstado(), "método de pago", venta.getMetodoPago(), "impuestos", venta.getImpuestos(), "total", venta.getTotal()));
            case "tableDepositoProducto":
                Page<ProductoDTO> productosPorDepositoPage = depositoProductoService.searchProductosByName(name, PageRequest.of(page, 10));
                return productosPorDepositoPage.map(producto -> Map.of("id", producto.getId(), "nombre", producto.getNombre(), "descripcion", producto.getDescripcion(), "stock", producto.getStockActual(), "macAddress", producto.getMacAddress(), "numeroDeSerie", producto.getNumeroDeSerie(), "codigoBarras", producto.getCodigoBarras(), "categoria", producto.getCategoriaNombre()));
            default:
                throw new IllegalArgumentException("Tabla no reconocida: " + table);
        }
    }
}