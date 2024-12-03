package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.DetalleVenta;
import com.interonda.inventory.dto.DetalleVentaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetalleVentaMapper {

    DetalleVentaDTO toDto(DetalleVenta detalleVenta);

    DetalleVenta toEntity(DetalleVentaDTO DetalleVentaDTO);
}
