package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.dto.DetalleCompraDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetalleCompraMapper {

    DetalleCompraDTO toDto(DetalleCompra detalleCompra);

    DetalleCompra toEntity(DetalleCompraDTO detalleCompraDTO);
}
