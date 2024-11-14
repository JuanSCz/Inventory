package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entityDTO.ProductoDTO;
import org.mapstruct.factory.Mappers;

public interface ProductoMapper {

    ProductoMapper INSTANCE = Mappers.getMapper(ProductoMapper.class);

    ProductoDTO toDto(Producto producto);

    Producto toEntity(ProductoDTO productoDTO);
}
