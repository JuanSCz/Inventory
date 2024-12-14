package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.entity.DetalleVenta;
import com.interonda.inventory.entity.Venta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    VentaDTO toDto(Venta venta);

    Venta toEntity(VentaDTO ventaDTO);

    DetalleVentaDTO toDetalleDto(DetalleVenta detalleVenta);

    DetalleVenta toDetalleEntity(DetalleVentaDTO detalleVentaDTO);
}