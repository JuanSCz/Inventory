package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.entityDTO.StockDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-15T12:39:12-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class StockMapperImpl implements StockMapper {

    @Override
    public StockDTO toDto(Stock stock) {
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

    @Override
    public Stock toEntity(StockDTO stockDTO) {
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
}
