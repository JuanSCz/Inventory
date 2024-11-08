package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Producto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


public interface ProductoService {
    List<Producto> findAll();

    Producto findById(Long id);

    Producto save(Producto producto);

    void deleteById(Long id);

    // Buscar por nombre
    List<Producto> buscarPorNombre(String nombre);

    //Buscar por categoria
    List<Producto> buscarPorCategoria(Long idCategoria);

    //Buscar por proveedor
    List<Producto> buscarPorProveedor(Long idProveedor);

    // Calcular productos que esten debajo del minimo.
    List<Producto> productosBajoStockMinimo();

    //Actualizar el Stock y registrar el cambio en historialStock
    Producto actualizarStock(Long idProducto, int cantidad, String motivo, String tipoMovimiento, Long idUsuario);

    // Calcular margen de Ganancia
    BigDecimal calcularMargenDeGanancia(Long idProducto);

    // Actualizar Precio
    Producto actualizarPrecio(Long idProducto, BigDecimal nuevoPrecio);

    // Actualizar Costo
    Producto actualizarCosto(Long idProducto, BigDecimal nuevoCosto);

    public Producto crearProducto(Producto producto, List<Long> idsProveedores);
}
