package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.DetalleVenta;
import com.interonda.Inventory.entityDTO.DetalleVentaDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-19T10:48:55-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
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
