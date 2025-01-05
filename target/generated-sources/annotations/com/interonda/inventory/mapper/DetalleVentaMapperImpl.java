package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.entity.DetalleVenta;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-04T22:44:26-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class DetalleVentaMapperImpl implements DetalleVentaMapper {

    @Override
    public DetalleVentaDTO toDto(DetalleVenta detalleVenta) {
        if ( detalleVenta == null ) {
            return null;
        }

        DetalleVentaDTO detalleVentaDTO = new DetalleVentaDTO();

        detalleVentaDTO.setId( detalleVenta.getId() );
        detalleVentaDTO.setCantidad( detalleVenta.getCantidad() );
        detalleVentaDTO.setPrecioUnitario( detalleVenta.getPrecioUnitario() );

        return detalleVentaDTO;
    }

    @Override
    public DetalleVenta toEntity(DetalleVentaDTO DetalleVentaDTO) {
        if ( DetalleVentaDTO == null ) {
            return null;
        }

        DetalleVenta detalleVenta = new DetalleVenta();

        detalleVenta.setId( DetalleVentaDTO.getId() );
        detalleVenta.setCantidad( DetalleVentaDTO.getCantidad() );
        detalleVenta.setPrecioUnitario( DetalleVentaDTO.getPrecioUnitario() );

        return detalleVenta;
    }
}
