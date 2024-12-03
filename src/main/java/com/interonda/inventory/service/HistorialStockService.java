package com.interonda.inventory.service;

import com.interonda.inventory.entity.HistorialStock;
import com.interonda.inventory.dto.HistorialStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistorialStockService {

    HistorialStockDTO convertToDto(HistorialStock historialStock);

    HistorialStock convertToEntity(HistorialStockDTO historialStockDTO);

    HistorialStockDTO createHistorialStock(HistorialStockDTO historialStockDTO);

    HistorialStockDTO updateHistorialStock(Long id, HistorialStockDTO historialStockDTO);

    void deleteHistorialStock(Long id);

    HistorialStockDTO getHistorialStock(Long id);

    Page<HistorialStockDTO> getAllHistorialStock(Pageable pageable);

}

