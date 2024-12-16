package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.entity.Producto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-15T14:54:32-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class ProductoMapperImpl implements ProductoMapper {

    @Override
    public ProductoDTO toDto(Producto producto) {
        if ( producto == null ) {
            return null;
        }

        ProductoDTO productoDTO = new ProductoDTO();

        productoDTO.setId( producto.getId() );
        productoDTO.setNombre( producto.getNombre() );
        productoDTO.setDescripcion( producto.getDescripcion() );
        productoDTO.setPrecio( producto.getPrecio() );
        productoDTO.setCosto( producto.getCosto() );
        productoDTO.setCodigoBarras( producto.getCodigoBarras() );
        productoDTO.setNumeroDeSerie( producto.getNumeroDeSerie() );
        productoDTO.setStockActual( producto.getStockActual() );
        productoDTO.setStockMinimo( producto.getStockMinimo() );
        productoDTO.setMacAddress( producto.getMacAddress() );

        return productoDTO;
    }

    @Override
    public Producto toEntity(ProductoDTO productoDTO) {
        if ( productoDTO == null ) {
            return null;
        }

        Producto producto = new Producto();

        producto.setId( productoDTO.getId() );
        producto.setNombre( productoDTO.getNombre() );
        producto.setPrecio( productoDTO.getPrecio() );
        producto.setDescripcion( productoDTO.getDescripcion() );
        producto.setCosto( productoDTO.getCosto() );
        producto.setCodigoBarras( productoDTO.getCodigoBarras() );
        producto.setNumeroDeSerie( productoDTO.getNumeroDeSerie() );
        producto.setStockActual( productoDTO.getStockActual() );
        producto.setStockMinimo( productoDTO.getStockMinimo() );
        producto.setMacAddress( productoDTO.getMacAddress() );

        return producto;
    }
}
