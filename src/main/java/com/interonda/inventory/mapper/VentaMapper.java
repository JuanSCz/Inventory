package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Venta;
import com.interonda.inventory.dto.VentaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    VentaDTO toDto(Venta venta);

    Venta toEntity(VentaDTO ventaDTO);
}
