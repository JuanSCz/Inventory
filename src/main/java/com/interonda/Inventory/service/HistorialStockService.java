package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.HistorialStock;

import java.util.List;
import java.util.Optional;

public interface HistorialStockService {
    List<HistorialStock> findAll();

    HistorialStock findById(Long id);

    HistorialStock save(HistorialStock historialStock);

    void deleteById(Long id);
}
