package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.RolDTO;
import com.interonda.inventory.entity.Rol;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T21:16:35-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class RolMapperImpl implements RolMapper {

    @Override
    public RolDTO toDto(Rol rol) {
        if ( rol == null ) {
            return null;
        }

        RolDTO rolDTO = new RolDTO();

        rolDTO.setId( rol.getId() );
        rolDTO.setNombre( rol.getNombre() );

        return rolDTO;
    }

    @Override
    public Rol toEntity(RolDTO rolDTO) {
        if ( rolDTO == null ) {
            return null;
        }

        Rol rol = new Rol();

        rol.setId( rolDTO.getId() );
        rol.setNombre( rolDTO.getNombre() );

        return rol;
    }
}
