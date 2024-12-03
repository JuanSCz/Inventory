package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.DetalleCompra;
import com.interonda.inventory.dto.DetalleCompraDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetalleCompraMapper {

    DetalleCompraDTO toDto(DetalleCompra detalleCompra);

    DetalleCompra toEntity(DetalleCompraDTO detalleCompraDTO);
}
