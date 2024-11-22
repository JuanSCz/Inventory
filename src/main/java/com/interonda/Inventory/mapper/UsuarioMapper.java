package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Usuario;
import com.interonda.Inventory.dto.UsuarioDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDto(Usuario usuario);

    Usuario toEntity(UsuarioDTO usuarioDTO);
}
