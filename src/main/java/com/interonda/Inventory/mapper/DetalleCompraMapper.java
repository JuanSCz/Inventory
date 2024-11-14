package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.entityDTO.DetalleCompraDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DetalleCompraMapper {
    DetalleCompraMapper INSTANCE = Mappers.getMapper(DetalleCompraMapper.class);

    DetalleCompraDTO toDto(DetalleCompra detalleCompra);

    DetalleCompra toEntity(DetalleCompraDTO detalleCompraDTO);
}
