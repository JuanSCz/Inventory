package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.entityDTO.HistorialStockDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HistorialStockMapper {

    HistorialStockMapper INSTANCE = Mappers.getMapper(HistorialStockMapper.class);

    HistorialStockDTO toDto(HistorialStock historialStock);

    HistorialStock toEntity(HistorialStockDTO historialStockDTO);
}
