package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.HistorialStock;
import com.interonda.inventory.dto.HistorialStockDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistorialStockMapper {

    HistorialStockDTO toDto(HistorialStock historialStock);

    HistorialStock toEntity(HistorialStockDTO historialStockDTO);
}
