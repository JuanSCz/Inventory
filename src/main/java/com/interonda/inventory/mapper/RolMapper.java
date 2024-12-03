package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Rol;
import com.interonda.inventory.dto.RolDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolDTO toDto(Rol rol);

    Rol toEntity(RolDTO rolDTO);
}
