package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Usuario;
import com.interonda.Inventory.entityDTO.UsuarioDTO;
import org.mapstruct.factory.Mappers;

public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDto(Usuario usuario);

    Usuario toEntity(UsuarioDTO usuarioDTO);
}
