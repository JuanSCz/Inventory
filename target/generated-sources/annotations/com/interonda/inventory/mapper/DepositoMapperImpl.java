package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.DepositoDTO;
import com.interonda.inventory.dto.StockDTO;
import com.interonda.inventory.entity.Deposito;
import com.interonda.inventory.entity.Stock;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-03T15:46:48-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class DepositoMapperImpl implements DepositoMapper {

    @Override
    public DepositoDTO toDto(Deposito deposito) {
        if ( deposito == null ) {
            return null;
        }

        DepositoDTO depositoDTO = new DepositoDTO();

        depositoDTO.setId( deposito.getId() );
        depositoDTO.setNombre( deposito.getNombre() );
        depositoDTO.setProvincia( deposito.getProvincia() );
        depositoDTO.setDireccion( deposito.getDireccion() );
        depositoDTO.setContactoDeposito( deposito.getContactoDeposito() );
        depositoDTO.setStocks( stockListToStockDTOList( deposito.getStocks() ) );

        return depositoDTO;
    }

    @Override
    public Deposito toEntity(DepositoDTO depositoDTO) {
        if ( depositoDTO == null ) {
            return null;
        }

        Deposito deposito = new Deposito();

        deposito.setId( depositoDTO.getId() );
        deposito.setNombre( depositoDTO.getNombre() );
        deposito.setProvincia( depositoDTO.getProvincia() );
        deposito.setDireccion( depositoDTO.getDireccion() );
        deposito.setStocks( stockDTOListToStockList( depositoDTO.getStocks() ) );
        deposito.setContactoDeposito( depositoDTO.getContactoDeposito() );

        return deposito;
    }

    @Override
    public void updateEntityFromDto(DepositoDTO depositoDTO, Deposito deposito) {
        if ( depositoDTO == null ) {
            return;
        }

        deposito.setId( depositoDTO.getId() );
        deposito.setNombre( depositoDTO.getNombre() );
        deposito.setProvincia( depositoDTO.getProvincia() );
        deposito.setDireccion( depositoDTO.getDireccion() );
        if ( deposito.getStocks() != null ) {
            List<Stock> list = stockDTOListToStockList( depositoDTO.getStocks() );
            if ( list != null ) {
                deposito.getStocks().clear();
                deposito.getStocks().addAll( list );
            }
            else {
                deposito.setStocks( null );
            }
        }
        else {
            List<Stock> list = stockDTOListToStockList( depositoDTO.getStocks() );
            if ( list != null ) {
                deposito.setStocks( list );
            }
        }
        deposito.setContactoDeposito( depositoDTO.getContactoDeposito() );
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
