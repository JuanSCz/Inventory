package com.interonda.Inventory.runner;

import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.service.ProductoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class MyAppRunner implements CommandLineRunner {

    private final ProductoService productoService;

    public MyAppRunner(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese un numero de categoria:");
        Long categoria = scanner.nextLong();

        List<Producto> productos = productoService.buscarPorCategoria(categoria);

        if (productos.isEmpty()) {
            System.out.println("Categoria no encontrada!");
        } else {
            System.out.println("Producto encontrado con exito!");
            productos.forEach(producto -> System.out.println("Categoria:" + categoria + " producto: " + producto.getNombre()));
        }
    }

    //Hibernate: select p1_0.id,p1_0.categoria_id,p1_0.codigo_barras,p1_0.costo,p1_0.descripcion,p1_0.imagen_producto,p1_0.nombre,p1_0.numero_de_serie,p1_0.precio,p1_0.stock_actual,p1_0.stock_minimo from productos p1_0 where p1_0.categoria_id=?
    //Hibernate: select c1_0.id,c1_0.descripcion,c1_0.nombre from categorias c1_0 where c1_0.id=?
}

