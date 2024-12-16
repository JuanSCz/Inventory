package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.UsuarioDTO;
import com.interonda.inventory.entity.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-15T14:54:30-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioDTO toDto(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId( usuario.getId() );
        usuarioDTO.setNombre( usuario.getNombre() );
        usuarioDTO.setContrasenia( usuario.getContrasenia() );
        usuarioDTO.setContacto( usuario.getContacto() );

        return usuarioDTO;
    }

    @Override
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( usuarioDTO.getId() );
        usuario.setNombre( usuarioDTO.getNombre() );
        usuario.setContrasenia( usuarioDTO.getContrasenia() );
        usuario.setContacto( usuarioDTO.getContacto() );

        return usuario;
    }
}
