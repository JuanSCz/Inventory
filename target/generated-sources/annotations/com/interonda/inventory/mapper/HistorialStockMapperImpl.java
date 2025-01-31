package com.interonda.inventory.mapper;

import com.interonda.inventory.dto.HistorialStockDTO;
import com.interonda.inventory.entity.HistorialStock;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-30T15:14:19-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
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
        historialStockDTO.setFechaActualizacion( historialStock.getFechaActualizacion() );
        historialStockDTO.setMotivo( historialStock.getMotivo() );
        historialStockDTO.setTipoMovimiento( historialStock.getTipoMovimiento() );

        return historialStockDTO;
    }

    @Override
    public HistorialStock toEntity(HistorialStockDTO historialStockDTO) {
        if ( historialStockDTO == null ) {
            return null;
        }

        HistorialStock historialStock = new HistorialStock();

        historialStock.setId( historialStockDTO.getId() );
        historialStock.setCantidadAnterior( historialStockDTO.getCantidadAnterior() );
        historialStock.setCantidadNueva( historialStockDTO.getCantidadNueva() );
        historialStock.setMotivo( historialStockDTO.getMotivo() );
        historialStock.setTipoMovimiento( historialStockDTO.getTipoMovimiento() );
        historialStock.setFechaActualizacion( historialStockDTO.getFechaActualizacion() );

        return historialStock;
    }
}
