package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.HistorialStock;
import com.interonda.Inventory.entityDTO.HistorialStockDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.HistorialStockMapper;
import com.interonda.Inventory.repository.HistorialStockRepository;
import com.interonda.Inventory.service.HistorialStockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialStockServiceImpl implements HistorialStockService {

    private static final Logger logger = LoggerFactory.getLogger(HistorialStockServiceImpl.class);

    private final HistorialStockRepository historialStockRepository;
    private final HistorialStockMapper historialStockMapper;

    @Autowired
    public HistorialStockServiceImpl(HistorialStockRepository historialStockRepository, HistorialStockMapper historialStockMapper) {
        this.historialStockRepository = historialStockRepository;
        this.historialStockMapper = historialStockMapper;
    }

    @Override
    public HistorialStockDTO convertToDto(HistorialStock historialStock) {
        return historialStockMapper.toDto(historialStock);
    }

    @Override
    public HistorialStock convertToEntity(HistorialStockDTO historialStockDTO) {
        return historialStockMapper.toEntity(historialStockDTO);
    }

    @Override
    @Transactional
    public HistorialStockDTO createHistorialStock(HistorialStockDTO historialStockDTO) {
        try {
            logger.info("Creando nuevo HistorialStock");
            HistorialStock historialStock = convertToEntity(historialStockDTO);
            HistorialStock savedHistorialStock = historialStockRepository.save(historialStock);
            logger.info("HistorialStock creado exitosamente con id: {}", savedHistorialStock.getId());
            return convertToDto(savedHistorialStock);
        } catch (Exception e) {
            logger.error("Error guardando HistorialStock", e);
            throw new DataAccessException("Error guardando HistorialStock", e);
        }
    }

    @Override
    @Transactional
    public HistorialStockDTO updateHistorialStock(Long id, HistorialStockDTO historialStockDTO) {
        try {
            logger.info("Actualizando HistorialStock con id: {}", id);
            HistorialStock historialStock = convertToEntity(historialStockDTO);
            historialStock.setId(id);
            HistorialStock updatedHistorialStock = historialStockRepository.save(historialStock);
            logger.info("HistorialStock actualizado exitosamente con id: {}", id);
            return convertToDto(updatedHistorialStock);
        } catch (Exception e) {
            logger.error("Error actualizando HistorialStock", e);
            throw new DataAccessException("Error actualizando HistorialStock", e);
        }
    }

    @Override
    @Transactional
    public void deleteHistorialStock(Long id) {
        try {
            logger.info("Eliminando HistorialStock con id: {}", id);
            historialStockRepository.deleteById(id);
            logger.info("HistorialStock eliminado exitosamente con id: {}", id);
        } catch (Exception e) {
            logger.error("Error eliminando HistorialStock", e);
            throw new DataAccessException("Error eliminando HistorialStock", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialStockDTO getHistorialStock(Long id) {
        try {
            logger.info("Obteniendo HistorialStock con id: {}", id);
            HistorialStock historialStock = historialStockRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("HistorialStock no encontrado"));
            return convertToDto(historialStock);
        } catch (Exception e) {
            logger.error("Error obteniendo HistorialStock", e);
            throw new DataAccessException("Error obteniendo HistorialStock", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialStockDTO> getAllHistorialStock() {
        try {
            logger.info("Obteniendo todos los HistorialStock");
            List<HistorialStock> historialStocks = historialStockRepository.findAll();
            return historialStocks.stream().map(historialStockMapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error obteniendo todos los HistorialStock", e);
            throw new DataAccessException("Error obteniendo todos los HistorialStock", e);
        }
    }
}