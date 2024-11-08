package com.interonda.Inventory.runner;

import com.interonda.Inventory.entity.Categoria;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.service.CategoriaService;
import com.interonda.Inventory.service.ProductoService;
import com.interonda.Inventory.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@Component
public class MyAppRunner implements CommandLineRunner {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private CategoriaService categoriaService; // Asegúrate de que tienes un servicio para obtener categorías

    @Override
    public void run(String... args) throws Exception {
        // Crear un nuevo producto

    }
}
