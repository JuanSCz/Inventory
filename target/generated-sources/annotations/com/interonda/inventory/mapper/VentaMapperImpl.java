package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.entity.Venta;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T16:16:02-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class VentaMapperImpl implements VentaMapper {

    @Override
    public VentaDTO toDto(Venta venta) {
        if ( venta == null ) {
            return null;
        }

        VentaDTO ventaDTO = new VentaDTO();

        ventaDTO.setId( venta.getId() );
        ventaDTO.setFecha( venta.getFecha() );
        ventaDTO.setTotal( venta.getTotal() );
        ventaDTO.setMetodoPago( venta.getMetodoPago() );
        ventaDTO.setEstado( venta.getEstado() );
        ventaDTO.setImpuestos( venta.getImpuestos() );

        return ventaDTO;
    }

    @Override
    public Venta toEntity(VentaDTO ventaDTO) {
        if ( ventaDTO == null ) {
            return null;
        }

        Venta venta = new Venta();

        venta.setId( ventaDTO.getId() );
        venta.setFecha( ventaDTO.getFecha() );
        venta.setTotal( ventaDTO.getTotal() );
        venta.setMetodoPago( ventaDTO.getMetodoPago() );
        venta.setEstado( ventaDTO.getEstado() );
        venta.setImpuestos( ventaDTO.getImpuestos() );

        return venta;
    }
}
