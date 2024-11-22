package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.dto.VentaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    VentaDTO toDto(Venta venta);

    Venta toEntity(VentaDTO ventaDTO);
}
