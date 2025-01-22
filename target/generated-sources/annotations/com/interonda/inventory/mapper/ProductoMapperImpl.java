package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.ProductoDTO;
import com.interonda.inventory.dto.StockDTO;
import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.entity.Stock;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-22T09:50:46-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
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
        productoDTO.setStocks( stockListToStockDTOList( producto.getStocks() ) );

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
        producto.setStocks( stockDTOListToStockList( productoDTO.getStocks() ) );

        return producto;
    }

    protected StockDTO stockToStockDTO(Stock stock) {
        if ( stock == null ) {
            return null;
        }

        StockDTO stockDTO = new StockDTO();

        stockDTO.setId( stock.getId() );
        stockDTO.setCantidad( stock.getCantidad() );
        stockDTO.setFechaActualizacion( stock.getFechaActualizacion() );
        stockDTO.setOperacion( stock.getOperacion() );

        return stockDTO;
    }

    protected List<StockDTO> stockListToStockDTOList(List<Stock> list) {
        if ( list == null ) {
            return null;
        }

        List<StockDTO> list1 = new ArrayList<StockDTO>( list.size() );
        for ( Stock stock : list ) {
            list1.add( stockToStockDTO( stock ) );
        }

        return list1;
    }

    protected Stock stockDTOToStock(StockDTO stockDTO) {
        if ( stockDTO == null ) {
            return null;
        }

        Stock stock = new Stock();

        stock.setId( stockDTO.getId() );
        stock.setCantidad( stockDTO.getCantidad() );
        stock.setFechaActualizacion( stockDTO.getFechaActualizacion() );
        stock.setOperacion( stockDTO.getOperacion() );

        return stock;
    }

    protected List<Stock> stockDTOListToStockList(List<StockDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Stock> list1 = new ArrayList<Stock>( list.size() );
        for ( StockDTO stockDTO : list ) {
            list1.add( stockDTOToStock( stockDTO ) );
        }

        return list1;
    }
}
