package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.DetalleVenta;
import com.interonda.Inventory.dto.DetalleVentaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetalleVentaMapper {

    DetalleVentaDTO toDto(DetalleVenta detalleVenta);

    DetalleVenta toEntity(DetalleVentaDTO DetalleVentaDTO);
}
