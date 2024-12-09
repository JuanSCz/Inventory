package com.interonda.inventory.service;

import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.dto.ProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {

    ProductoDTO convertToDto(Producto producto);

    Producto convertToEntity(ProductoDTO productoDTO);

    ProductoDTO createProducto(ProductoDTO productoDTO);

    ProductoDTO updateProducto(ProductoDTO productoDTO);

    void deleteProducto(Long id);

    ProductoDTO getProducto(Long id);

    Page<ProductoDTO> getAllProductos(Pageable pageable);

    long countProductos();
}
