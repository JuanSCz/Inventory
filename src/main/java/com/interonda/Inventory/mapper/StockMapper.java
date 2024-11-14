package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.entityDTO.StockDTO;
import org.mapstruct.factory.Mappers;

public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    StockDTO toDto(Stock stock);

    Stock toEntity(StockDTO stockDTO);
}
