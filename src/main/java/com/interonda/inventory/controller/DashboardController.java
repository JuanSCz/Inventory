package com.interonda.inventory.controller;

import com.interonda.inventory.service.*;
import com.interonda.inventory.service.impl.VentaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ClienteService clienteService;
    private final CompraService compraService;
    private final DepositoService depositoService;
    private final ProveedorService proveedorService;
    private final UsuarioService usuarioService;
    private final VentaService ventaService;

    @Autowired
    public DashboardController(ProductoService productoService, CategoriaService categoriaService, ClienteService clienteService, CompraService compraService, DepositoService depositoService, ProveedorService proveedorService, UsuarioService usuarioService, VentaService ventaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.clienteService = clienteService;
        this.compraService = compraService;
        this.depositoService = depositoService;
        this.proveedorService = proveedorService;
        this.usuarioService = usuarioService;
        this.ventaService = ventaService;
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        long totalProductos = productoService.countProductos();
        long totalCategorias = categoriaService.countCategorias();
        long totalClientes = clienteService.countClientes();
        long totalCompras = compraService.countCompras();
        long totalDepositos = depositoService.countDepositos();
        long totalProveedores = proveedorService.countProveedores();
        long totalUsuarios = usuarioService.countUsuarios();
        long totalVentas = ventaService.countVentas();

        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalCategorias", totalCategorias);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalCompras", compraService.countCompras());
        model.addAttribute("totalDepositos", depositoService.countDepositos());
        model.addAttribute("totalProveedores", proveedorService.countProveedores());
        model.addAttribute("totalUsuarios", usuarioService.countUsuarios());
        model.addAttribute("totalVentas", ventaService.countVentas());
        return "dashboard";
    }


}
