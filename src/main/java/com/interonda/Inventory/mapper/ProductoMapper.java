package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.dto.ProductoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoDTO toDto(Producto producto);

    Producto toEntity(ProductoDTO productoDTO);
}
