package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.dto.HistorialStockDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistorialStockMapper {

    HistorialStockDTO toDto(HistorialStock historialStock);

    HistorialStock toEntity(HistorialStockDTO historialStockDTO);
}
