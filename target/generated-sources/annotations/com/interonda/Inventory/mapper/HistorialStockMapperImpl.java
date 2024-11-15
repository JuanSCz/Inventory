package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.entityDTO.HistorialStockDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-15T09:10:59-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class HistorialStockMapperImpl implements HistorialStockMapper {

    @Override
    public HistorialStockDTO toDto(HistorialStock historialStock) {
        if ( historialStock == null ) {
            return null;
        }

        HistorialStockDTO historialStockDTO = new HistorialStockDTO();

        historialStockDTO.setId( historialStock.getId() );
        historialStockDTO.setCantidadAnterior( historialStock.getCantidadAnterior() );
        historialStockDTO.setCantidadNueva( historialStock.getCantidadNueva() );
        historialStockDTO.setMotivo( historialStock.getMotivo() );
        historialStockDTO.setTipoMovimiento( historialStock.getTipoMovimiento() );
        historialStockDTO.setObservacion( historialStock.getObservacion() );

        return historialStockDTO;
    }

    @Override
    public HistorialStock toEntity(HistorialStockDTO historialStockDTO) {
        if ( historialStockDTO == null ) {
            return null;
        }

        HistorialStock historialStock = new HistorialStock();

        historialStock.setId( historialStockDTO.getId() );
        if ( historialStockDTO.getCantidadAnterior() != null ) {
            historialStock.setCantidadAnterior( historialStockDTO.getCantidadAnterior() );
        }
        if ( historialStockDTO.getCantidadNueva() != null ) {
            historialStock.setCantidadNueva( historialStockDTO.getCantidadNueva() );
        }
        historialStock.setMotivo( historialStockDTO.getMotivo() );
        historialStock.setTipoMovimiento( historialStockDTO.getTipoMovimiento() );
        historialStock.setObservacion( historialStockDTO.getObservacion() );

        return historialStock;
    }
}
