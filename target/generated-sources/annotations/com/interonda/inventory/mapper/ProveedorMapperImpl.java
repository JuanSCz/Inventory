package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.ProveedorDTO;
import com.interonda.inventory.entity.Proveedor;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-10T16:26:17-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProveedorMapperImpl implements ProveedorMapper {

    @Override
    public ProveedorDTO toDto(Proveedor proveedor) {
        if ( proveedor == null ) {
            return null;
        }

        ProveedorDTO proveedorDTO = new ProveedorDTO();

        proveedorDTO.setId( proveedor.getId() );
        proveedorDTO.setNombre( proveedor.getNombre() );
        proveedorDTO.setContactoProveedor( proveedor.getContactoProveedor() );
        proveedorDTO.setDireccion( proveedor.getDireccion() );
        proveedorDTO.setPais( proveedor.getPais() );
        proveedorDTO.setEmailProveedor( proveedor.getEmailProveedor() );

        return proveedorDTO;
    }

    @Override
    public Proveedor toEntity(ProveedorDTO proveedorDTO) {
        if ( proveedorDTO == null ) {
            return null;
        }

        Proveedor proveedor = new Proveedor();

        proveedor.setId( proveedorDTO.getId() );
        proveedor.setNombre( proveedorDTO.getNombre() );
        proveedor.setContactoProveedor( proveedorDTO.getContactoProveedor() );
        proveedor.setDireccion( proveedorDTO.getDireccion() );
        proveedor.setPais( proveedorDTO.getPais() );
        proveedor.setEmailProveedor( proveedorDTO.getEmailProveedor() );

        return proveedor;
    }
}
