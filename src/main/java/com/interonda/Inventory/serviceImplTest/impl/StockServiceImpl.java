package com.interonda.Inventory.serviceImplTest.impl;

import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.dto.StockDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.StockMapper;
import com.interonda.Inventory.repository.StockRepository;
import com.interonda.Inventory.serviceImplTest.StockService;

import com.interonda.Inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl implements StockService {

    private static Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final Validator validator;


    @Autowired
    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper, Validator validator) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.validator = validator;
    }

    // Constructor for testing purposes
    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper, Validator validator, Logger logger) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.validator = validator;
        this.logger = logger;
    }

    @Override
    public StockDTO convertToDto(Stock stock) {
        return stockMapper.toDto(stock);
    }

    @Override
    public Stock convertToEntity(StockDTO stockDTO) {
        return stockMapper.toEntity(stockDTO);
    }

    @Override
    @Transactional
    public StockDTO createStock(StockDTO stockDTO) {
        try {
            ValidatorUtils.validateEntity(stockDTO, validator);
            logger.info("Creando nuevo Stock con cantidad: {}", stockDTO.getCantidad());
            Stock stock = stockMapper.toEntity(stockDTO);
            Stock savedStock = stockRepository.save(stock);
            logger.info("Stock creado exitosamente con id: {}", savedStock.getId());
            return stockMapper.toDto(savedStock);
        } catch (Exception e) {
            logger.error("Error creando Stock", e);
            throw new DataAccessException("Error creando Stock", e);
        }
    }

    @Override
    @Transactional
    public StockDTO updateStock(StockDTO stockDTO) {
        ValidatorUtils.validateEntity(stockDTO, validator);
        try {
            logger.info("Actualizando Stock con id: {}", stockDTO.getId());
            Stock stock = stockRepository.findById(stockDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado con el id: " + stockDTO.getId()));
            stock = stockMapper.toEntity(stockDTO);
            Stock updatedStock = stockRepository.save(stock);
            logger.info("Stock actualizado exitosamente con id: {}", updatedStock.getId());
            return stockMapper.toDto(updatedStock);
        } catch (ResourceNotFoundException e) {
            logger.warn("Stock no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Stock", e);
            throw new DataAccessException("Error actualizando Stock", e);
        }
    }

    @Override
    @Transactional
    public void deleteStock(Long id) {
        try {
            logger.info("Eliminando Stock con id: {}", id);
            if (!stockRepository.existsById(id)) {
                throw new ResourceNotFoundException("Stock no encontrado con el id: " + id);
            }
            stockRepository.deleteById(id);
            logger.info("Stock eliminado exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Stock no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Stock", e);
            throw new DataAccessException("Error eliminando Stock", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StockDTO getStock(Long id) {
        try {
            logger.info("Obteniendo Stock con id: {}", id);
            Stock stock = stockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado con el id: " + id));
            return stockMapper.toDto(stock);
        } catch (ResourceNotFoundException e) {
            logger.warn("Stock no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Stock", e);
            throw new DataAccessException("Error obteniendo Stock", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockDTO> getAllStocks(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Stocks con paginación");
            Page<Stock> stockPage = stockRepository.findAll(pageable);
            return stockPage.map(stockMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Stocks con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Stocks con paginación", e);
        }
    }
}
