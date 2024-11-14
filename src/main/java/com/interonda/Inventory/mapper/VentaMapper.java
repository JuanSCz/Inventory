package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.entityDTO.VentaDTO;
import org.mapstruct.factory.Mappers;

public interface VentaMapper {

    VentaMapper INSTANCE = Mappers.getMapper(VentaMapper.class);

    VentaDTO toDto(Venta venta);

    Venta toEntity(VentaDTO ventaDTO);
}
