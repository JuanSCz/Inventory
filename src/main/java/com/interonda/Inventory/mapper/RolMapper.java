package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Rol;
import com.interonda.Inventory.dto.RolDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolDTO toDto(Rol rol);

    Rol toEntity(RolDTO rolDTO);
}
