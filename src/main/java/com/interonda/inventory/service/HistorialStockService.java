package com.interonda.inventory.service;

import com.interonda.inventory.entity.HistorialStock;
import com.interonda.inventory.dto.HistorialStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HistorialStockService {

    HistorialStockDTO convertToDto(HistorialStock historialStock);

    HistorialStock convertToEntity(HistorialStockDTO historialStockDTO);

    HistorialStockDTO createHistorialStock(HistorialStockDTO historialStockDTO);

    HistorialStockDTO getHistorialStock(Long id);

    Page<HistorialStockDTO> getAllHistorialStock(Pageable pageable);

}