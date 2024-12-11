package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Compra;
import com.interonda.inventory.entity.DetalleCompra;
import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.DetalleCompraDTO;

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