package com.interonda.inventory.service;

import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.dto.ProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductoService {

    ProductoDTO convertToDto(Producto producto);

    Producto convertToEntity(ProductoDTO productoDTO);

    ProductoDTO createProducto(ProductoDTO productoDTO);

    ProductoDTO updateProducto(ProductoDTO productoDTO);

    boolean deleteProducto(Long id);

    ProductoDTO getProducto(Long id);

    Page<ProductoDTO> getAllProductos(Pageable pageable);

    Page<ProductoDTO> searchProductosByName(String nombre, Pageable pageable);

    long countProductos();

    List<Producto> obtenerTodosLosProductos();
}
