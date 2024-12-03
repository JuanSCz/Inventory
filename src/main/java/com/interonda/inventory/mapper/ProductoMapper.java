package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.dto.ProductoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoDTO toDto(Producto producto);

    Producto toEntity(ProductoDTO productoDTO);
}
