package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.dto.VentaDTO;
import com.interonda.inventory.entity.DetalleVenta;
import com.interonda.inventory.entity.Venta;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-18T15:34:58-0300",
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
        ventaDTO.setDetallesVenta( detalleVentaListToDetalleVentaDTOList( venta.getDetallesVenta() ) );

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
        venta.setDetallesVenta( detalleVentaDTOListToDetalleVentaList( ventaDTO.getDetallesVenta() ) );

        return venta;
    }

    @Override
    public DetalleVentaDTO toDetalleDto(DetalleVenta detalleVenta) {
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
    public DetalleVenta toDetalleEntity(DetalleVentaDTO detalleVentaDTO) {
        if ( detalleVentaDTO == null ) {
            return null;
        }

        DetalleVenta detalleVenta = new DetalleVenta();

        detalleVenta.setId( detalleVentaDTO.getId() );
        detalleVenta.setCantidad( detalleVentaDTO.getCantidad() );
        detalleVenta.setPrecioUnitario( detalleVentaDTO.getPrecioUnitario() );

        return detalleVenta;
    }

    protected List<DetalleVentaDTO> detalleVentaListToDetalleVentaDTOList(List<DetalleVenta> list) {
        if ( list == null ) {
            return null;
        }

        List<DetalleVentaDTO> list1 = new ArrayList<DetalleVentaDTO>( list.size() );
        for ( DetalleVenta detalleVenta : list ) {
            list1.add( toDetalleDto( detalleVenta ) );
        }

        return list1;
    }

    protected List<DetalleVenta> detalleVentaDTOListToDetalleVentaList(List<DetalleVentaDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<DetalleVenta> list1 = new ArrayList<DetalleVenta>( list.size() );
        for ( DetalleVentaDTO detalleVentaDTO : list ) {
            list1.add( toDetalleEntity( detalleVentaDTO ) );
        }

        return list1;
    }
}
