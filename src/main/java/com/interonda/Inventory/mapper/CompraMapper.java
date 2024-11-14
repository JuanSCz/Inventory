package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.entityDTO.CompraDTO;
import com.interonda.Inventory.entityDTO.DetalleCompraDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompraMapper {
    CompraMapper INSTANCE = Mappers.getMapper(CompraMapper.class);

    CompraDTO toDto(Compra compra);

    Compra toEntity(CompraDTO compraDTO);

    DetalleCompraDTO toDetalleCompraDto(DetalleCompra detalleCompra);

    DetalleCompra toDetalleCompraEntity(DetalleCompraDTO detalleCompraDTO);
}
