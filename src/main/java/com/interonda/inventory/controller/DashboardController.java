package com.interonda.inventory.controller;

import com.interonda.inventory.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ClienteService clienteService;
    private final CompraService compraService;
    private final DepositoService depositoService;
    private final ProveedorService proveedorService;
    private final UsuarioService usuarioService;
 /*   private final VentaService ventaService; */

    @Autowired
    public DashboardController(ProductoService productoService, CategoriaService categoriaService, ClienteService clienteService, CompraService compraService, DepositoService depositoService, ProveedorService proveedorService, UsuarioService usuarioService /* VentaService ventaService */) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.clienteService = clienteService;
        this.compraService = compraService;
        this.depositoService = depositoService;
        this.proveedorService = proveedorService;
        this.usuarioService = usuarioService;
      /*  this.ventaService = ventaService; */
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
     /*  long totalVentas = ventaService.countVentas(); */

        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalCategorias", totalCategorias);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalCompras", totalCompras);
        model.addAttribute("totalDepositos", totalDepositos);
        model.addAttribute("totalProveedores", totalProveedores);
        model.addAttribute("totalUsuarios", totalUsuarios);
      /*  model.addAttribute("totalVentas", totalVentas); */
        model.addAttribute("currentPage", "dashboard");
        return "dashboard";
    }


    @GetMapping("/chartProveedores")
    public String mostrarGraficoProveedores(Model model) {
        // Simulación de datos para el gráfico
        List<String> labels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "chartProveedores";
    }

    @GetMapping("/chartProductos")
    public String mostrarGraficoProductos(Model model) {
        // Simulación de datos para el gráfico
        List<String> labels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "chartProductos";
    }

    @GetMapping("/chartClientes")
    public String mostrarGraficoClientes(Model model) {
        // Simulación de datos para el gráfico
        List<String> labels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "chartClientes";
    }

    @GetMapping("/chartCompras")
    public String mostrarGraficoCompras(Model model) {
        // Simulación de datos para el gráfico
        List<String> labels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "chartCompras";
    }

    @GetMapping("/chartVentas")
    public String mostrarGraficoVentas(Model model) {
        // Simulación de datos para el gráfico
        List<String> labels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "chartVentas";
    }

    @GetMapping("/chartUsuarios")
    public String mostrarGraficoUsuarios(Model model) {
        // Simulación de datos para el gráfico
        List<String> labels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "chartUsuarios";
    }

    @GetMapping("/chartDepositos")
    public String mostrarGraficoDepositos(Model model) {
        // Simulación de datos para el gráfico
        List<String> labels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "chartDepositos";
    }

    @GetMapping("/chartCategorias")
    public String mostrarGraficoCategorias(Model model) {
        // Simulación de datos para el gráfico
        List<String> labels = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120);

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "chartCategorias";
    }


}
