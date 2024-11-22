package com.interonda.Inventory.mapper;

import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.dto.StockDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockDTO toDto(Stock stock);

    Stock toEntity(StockDTO stockDTO);
}
