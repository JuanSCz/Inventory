package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.entity.Compra;
import com.interonda.inventory.entity.DetalleCompra;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-11T12:20:37-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CompraMapperImpl implements CompraMapper {

    @Override
    public CompraDTO toDto(Compra compra) {
        if ( compra == null ) {
            return null;
        }

        CompraDTO compraDTO = new CompraDTO();

        compraDTO.setId( compra.getId() );
        compraDTO.setFecha( compra.getFecha() );
        compraDTO.setTotal( compra.getTotal() );
        compraDTO.setMetodoPago( compra.getMetodoPago() );
        compraDTO.setEstado( compra.getEstado() );
        compraDTO.setImpuestos( compra.getImpuestos() );
        compraDTO.setDetallesCompra( detalleCompraListToDetalleCompraDTOList( compra.getDetallesCompra() ) );

        return compraDTO;
    }

    @Override
    public Compra toEntity(CompraDTO compraDTO) {
        if ( compraDTO == null ) {
            return null;
        }

        Compra compra = new Compra();

        compra.setId( compraDTO.getId() );
        compra.setFecha( compraDTO.getFecha() );
        compra.setTotal( compraDTO.getTotal() );
        compra.setMetodoPago( compraDTO.getMetodoPago() );
        compra.setEstado( compraDTO.getEstado() );
        compra.setImpuestos( compraDTO.getImpuestos() );
        compra.setDetallesCompra( detalleCompraDTOListToDetalleCompraList( compraDTO.getDetallesCompra() ) );

        return compra;
    }

    @Override
    public DetalleCompraDTO toDetalleDto(DetalleCompra detalleCompra) {
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
    public DetalleCompra toDetalleEntity(DetalleCompraDTO detalleCompraDTO) {
        if ( detalleCompraDTO == null ) {
            return null;
        }

        DetalleCompra detalleCompra = new DetalleCompra();

        detalleCompra.setId( detalleCompraDTO.getId() );
        detalleCompra.setCantidad( detalleCompraDTO.getCantidad() );
        detalleCompra.setPrecioUnitario( detalleCompraDTO.getPrecioUnitario() );

        return detalleCompra;
    }

    protected List<DetalleCompraDTO> detalleCompraListToDetalleCompraDTOList(List<DetalleCompra> list) {
        if ( list == null ) {
            return null;
        }

        List<DetalleCompraDTO> list1 = new ArrayList<DetalleCompraDTO>( list.size() );
        for ( DetalleCompra detalleCompra : list ) {
            list1.add( toDetalleDto( detalleCompra ) );
        }

        return list1;
    }

    protected List<DetalleCompra> detalleCompraDTOListToDetalleCompraList(List<DetalleCompraDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<DetalleCompra> list1 = new ArrayList<DetalleCompra>( list.size() );
        for ( DetalleCompraDTO detalleCompraDTO : list ) {
            list1.add( toDetalleEntity( detalleCompraDTO ) );
        }

        return list1;
    }
}
