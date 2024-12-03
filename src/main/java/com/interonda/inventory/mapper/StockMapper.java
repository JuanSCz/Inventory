package com.interonda.inventory.mapper;

import com.interonda.inventory.entity.Stock;
import com.interonda.inventory.dto.StockDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockDTO toDto(Stock stock);

    Stock toEntity(StockDTO stockDTO);
}
