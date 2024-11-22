package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.dto.ProveedorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProveedorMapper {

    ProveedorDTO toDto(Proveedor proveedor);

    Proveedor toEntity(ProveedorDTO proveedorDTO);
}
