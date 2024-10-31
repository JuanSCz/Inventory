package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Stock;

import java.util.List;
import java.util.Optional;

public interface StockService {
    List<Stock> findAll();

    Optional<Stock> findById(Long id);

    Stock save(Stock stock);

    void deleteById(Long id);

}
