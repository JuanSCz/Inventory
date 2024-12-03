package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Proveedor;
import com.interonda.inventory.dto.ProveedorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProveedorMapper {

    ProveedorDTO toDto(Proveedor proveedor);

    Proveedor toEntity(ProveedorDTO proveedorDTO);
}
