package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.entity.DetalleCompra;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-05T08:19:49-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class DetalleCompraMapperImpl implements DetalleCompraMapper {

    @Override
    public DetalleCompraDTO toDto(DetalleCompra detalleCompra) {
        if ( detalleCompra == null ) {
            return null;
        }

        DetalleCompraDTO detalleCompraDTO = new DetalleCompraDTO();

        detalleCompraDTO.setId( detalleCompra.getId() );
        detalleCompraDTO.setCantidad( detalleCompra.getCantidad() );
        detalleCompraDTO.setPrecioUnitario( detalleCompra.getPrecioUnitario() );

        return detalleCompraDTO;
    }

    @Override
    public DetalleCompra toEntity(DetalleCompraDTO detalleCompraDTO) {
        if ( detalleCompraDTO == null ) {
            return null;
        }

        DetalleCompra detalleCompra = new DetalleCompra();

        detalleCompra.setId( detalleCompraDTO.getId() );
        detalleCompra.setCantidad( detalleCompraDTO.getCantidad() );
        detalleCompra.setPrecioUnitario( detalleCompraDTO.getPrecioUnitario() );

        return detalleCompra;
    }
}
