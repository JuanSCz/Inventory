package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Usuario;
import com.interonda.inventory.dto.UsuarioDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDto(Usuario usuario);

    Usuario toEntity(UsuarioDTO usuarioDTO);
}
