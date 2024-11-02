package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.exception.ResourceNotFoundException;
import com.interonda.Inventory.repository.HistorialStockRepository;
import com.interonda.Inventory.service.HistorialStockService;

import java.util.List;
import java.util.Optional;

public class HistorialStockServiceImpl implements HistorialStockService {

    private final HistorialStockRepository historialStockRepository;

    public HistorialStockServiceImpl(HistorialStockRepository historialStockRepository) {
        this.historialStockRepository = historialStockRepository;
    }

    @Override
    public List<HistorialStock> findAll() {
        return historialStockRepository.findAll();
    }

    @Override
    public HistorialStock findById(Long id) {
        return historialStockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El historial de stock N " + id + " no fue encontrado!"));
    }

    @Override
    public HistorialStock save(HistorialStock historialStock) {
        return historialStockRepository.save(historialStock);
    }

    @Override
    public void deleteById(Long id) {
        historialStockRepository.deleteById(id);
    }
}
