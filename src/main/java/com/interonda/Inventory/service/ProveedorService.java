package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entity.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProveedorService {
    List<Proveedor> findAll();

    Proveedor findById(Long id);

    Proveedor save(Proveedor proveedor);

    void deleteById(Long id);

    Proveedor crearProveedor(Proveedor proveedor);

    Proveedor actualizarProveedor(Long id, Proveedor proveedorActualizado);

    void eliminarProveedor(Long id);

    Page<Proveedor> listarProveedores(Pageable pageable);

    Proveedor buscarProveedorPorId(Long id);

    List<Proveedor> buscarProveedoresPorNombre(String nombre);

    void vincularProductoAProveedor(Long proveedorId, Long productoId);

    void desvincularProductoDeProveedor(Long proveedorId, Long productoId);

    List<Producto> obtenerProductosDeProveedor(Long proveedorId);

    Compra registrarCompra(Long proveedorId, Compra compra);

    List<Compra> obtenerComprasDeProveedor(Long proveedorId);

}
