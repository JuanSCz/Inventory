package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.StockDTO;
import com.interonda.inventory.entity.Stock;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-08T23:57:03-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
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
