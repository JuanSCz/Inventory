package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.repository.StockRepository;
import com.interonda.Inventory.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StockServiceImpl implements StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    @Override
    public Stock findById(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El stock N " + id + " no fue encontrado!"));
    }

    @Override
    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public void deleteById(Long id) {
        stockRepository.deleteById(id);
    }
}
