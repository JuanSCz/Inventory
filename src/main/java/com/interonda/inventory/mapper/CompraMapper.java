package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.entity.Compra;

import com.interonda.inventory.dto.CompraDTO;

import com.interonda.inventory.entity.DetalleCompra;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CompraMapper {
    CompraDTO toDto(Compra compra);

    Compra toEntity(CompraDTO compraDTO);

    DetalleCompraDTO toDetalleDto(DetalleCompra detalleCompra);

    DetalleCompra toDetalleEntity(DetalleCompraDTO detalleCompraDTO);
}