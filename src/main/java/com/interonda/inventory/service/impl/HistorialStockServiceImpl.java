package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.HistorialStockDTO;
import com.interonda.inventory.entity.HistorialStock;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.HistorialStockMapper;
import com.interonda.inventory.repository.HistorialStockRepository;
import com.interonda.inventory.service.HistorialStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
            HistorialStock historialStock = historialStockMapper.toEntity(historialStockDTO);
            HistorialStock savedHistorialStock = historialStockRepository.save(historialStock);
            return historialStockMapper.toDto(savedHistorialStock);
        } catch (Exception e) {
            throw new DataAccessException("Error creando HistorialStock", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialStockDTO getHistorialStock(Long id) {
        try {
            logger.info("Obteniendo HistorialStock con id: {}", id);
            HistorialStock historialStock = historialStockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HistorialStock no encontrado"));
            HistorialStockDTO historialStockDTO = convertToDto(historialStock);
            historialStockDTO.setProductoNombre(historialStock.getProducto().getNombre());
            historialStockDTO.setDepositoNombre(historialStock.getDeposito().getNombre());
            return historialStockDTO;
        } catch (ResourceNotFoundException e) {
            logger.warn("HistorialStock no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo HistorialStock", e);
            throw new DataAccessException("Error obteniendo HistorialStock", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistorialStockDTO> getAllHistorialStock(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los HistorialStock con paginación");
            Page<HistorialStock> historialStockPage = historialStockRepository.findAll(pageable);
            return historialStockPage.map(historialStock -> {
                HistorialStockDTO historialStockDTO = historialStockMapper.toDto(historialStock);
                historialStockDTO.setProductoNombre(historialStock.getProducto().getNombre());
                historialStockDTO.setDepositoNombre(historialStock.getDeposito().getNombre());
                return historialStockDTO;
            });
        } catch (Exception e) {
            logger.error("Error obteniendo todos los HistorialStock con paginación", e);
            throw new DataAccessException("Error obteniendo todos los HistorialStock con paginación", e);
        }
    }
}