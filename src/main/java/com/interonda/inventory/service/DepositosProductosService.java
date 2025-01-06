package com.interonda.inventory.service;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepositosProductosService {
    ProductoDTO convertToDto(Producto producto);

    Producto convertToEntity(ProductoDTO productoDTO);

    ProductoDTO updateProducto(ProductoDTO productoDTO);

    ProductoDTO getProducto(Long id);

    Page<ProductoDTO> getAllProductos(Pageable pageable);

    Page<ProductoDTO> searchProductosByName(String nombre, Pageable pageable);

    long countProductos();

    List<Producto> obtenerTodosLosProductos();

    Page<ProductoDTO> getProductosByDeposito(Long depositoId, Pageable pageable);
}
