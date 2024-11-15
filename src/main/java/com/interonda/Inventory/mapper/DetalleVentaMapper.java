package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.DetalleVenta;
import com.interonda.Inventory.entityDTO.DetalleVentaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DetalleVentaMapper {
    DetalleVentaMapper INSTANCE = Mappers.getMapper(DetalleVentaMapper.class);

    DetalleVentaDTO toDto(DetalleVenta detalleVenta);

    DetalleVenta toEntity(DetalleVentaDTO DetalleVentaDTO);
}
