package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entityDTO.ProductoDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface ProductoService {

    ProductoDTO convertToDto(Producto producto);

    Producto convertToEntity(ProductoDTO productoDTO);

    ProductoDTO createProducto(ProductoDTO productoDTO);

    ProductoDTO updateProducto(ProductoDTO productoDTO);

    void deleteProducto(Long id);

    ProductoDTO getProducto(Long id);

    List<ProductoDTO> getAllProductos();


}
