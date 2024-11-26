package com.interonda.Inventory.mapper;

import com.interonda.Inventory.dto.UsuarioDTO;
import com.interonda.Inventory.entity.Usuario;
import java.util.Arrays;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-26T13:59:52-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
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
        byte[] imagenUsuario = usuario.getImagenUsuario();
        if ( imagenUsuario != null ) {
            usuarioDTO.setImagenUsuario( Arrays.copyOf( imagenUsuario, imagenUsuario.length ) );
        }
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
        byte[] imagenUsuario = usuarioDTO.getImagenUsuario();
        if ( imagenUsuario != null ) {
            usuario.setImagenUsuario( Arrays.copyOf( imagenUsuario, imagenUsuario.length ) );
        }
        usuario.setContacto( usuarioDTO.getContacto() );

        return usuario;
    }
}
