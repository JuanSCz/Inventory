package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.dto.CompraDTO;
import com.interonda.Inventory.dto.DetalleCompraDTO;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompraMapper {
    CompraDTO toDto(Compra compra);

    Compra toEntity(CompraDTO compraDTO);

    void updateEntityFromDto(CompraDTO compraDTO, @MappingTarget Compra compra);

    DetalleCompraDTO toDetalleCompraDto(DetalleCompra detalleCompra);

    DetalleCompra toDetalleCompraEntity(DetalleCompraDTO detalleCompraDTO);
}