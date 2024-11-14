package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.entityDTO.HistorialStockDTO;

import java.util.List;

public interface HistorialStockService {

    HistorialStockDTO convertToDto(HistorialStock historialStock);

    HistorialStock convertToEntity(HistorialStockDTO historialStockDTO);

    HistorialStockDTO createHistorialStock(HistorialStockDTO historialStockDTO);

    HistorialStockDTO updateHistorialStock(Long id, HistorialStockDTO historialStockDTO);

    public void deleteHistorialStock(Long id);

    public HistorialStockDTO getHistorialStock(Long id);

    public List<HistorialStockDTO> getAllHistorialStock();

}

