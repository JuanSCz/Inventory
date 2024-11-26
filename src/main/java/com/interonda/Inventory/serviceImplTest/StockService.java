package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.dto.StockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockService {

    StockDTO convertToDto(Stock stock);

    Stock convertToEntity(StockDTO stockDTO);

    StockDTO createStock(StockDTO stockDTO);

    StockDTO updateStock(StockDTO stockDTO);

    void deleteStock(Long id);

    StockDTO getStock(Long id);

    Page<StockDTO> getAllStocks(Pageable pageable);
}