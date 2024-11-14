package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.entityDTO.ProveedorDTO;
import org.mapstruct.factory.Mappers;

public interface ProveedorMapper {

    ProveedorMapper INSTANCE = Mappers.getMapper(ProveedorMapper.class);

    ProveedorDTO toDto(Proveedor proveedor);

    Proveedor toEntity(ProveedorDTO proveedorDTO);
}
