package com.interonda.inventory.controller;

import com.interonda.inventory.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ProductoService productoService;

    @Autowired
    public DashboardController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        long totalProductos = productoService.countProductos();
        model.addAttribute("totalProductos", totalProductos);
        return "dashboard";
    }
}
