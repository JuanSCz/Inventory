package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Rol;
import com.interonda.Inventory.entityDTO.RolDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RolMapper {

    RolMapper INSTANCE = Mappers.getMapper(RolMapper.class);

    RolDTO toDto(Rol rol);

    Rol toEntity(RolDTO rolDTO);
}
