package com.interonda.inventory.service.impl;

import com.interonda.inventory.entity.HistorialStock;
import com.interonda.inventory.dto.HistorialStockDTO;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.HistorialStockMapper;
import com.interonda.inventory.repository.*;
import com.interonda.inventory.service.HistorialStockService;

import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistorialStockServiceImpl implements HistorialStockService {

    private static final Logger logger = LoggerFactory.getLogger(HistorialStockServiceImpl.class);

    private final HistorialStockRepository historialStockRepository;
    private final HistorialStockMapper historialStockMapper;
    private final ProductoRepository productoRepository;
    private final DepositoRepository depositoRepository;
    private final UsuarioRepository usuarioRepository;
    private final StockRepository stockRepository;
    private final Validator validator;

    @Autowired
    public HistorialStockServiceImpl(HistorialStockRepository historialStockRepository, HistorialStockMapper historialStockMapper, ProductoRepository productoRepository, DepositoRepository depositoRepository, UsuarioRepository usuarioRepository, StockRepository stockRepository, Validator validator) {
        this.historialStockRepository = historialStockRepository;
        this.historialStockMapper = historialStockMapper;
        this.productoRepository = productoRepository;
        this.depositoRepository = depositoRepository;
        this.usuarioRepository = usuarioRepository;
        this.stockRepository = stockRepository;
        this.validator = validator;
    }

    @Override
    public HistorialStockDTO convertToDto(HistorialStock historialStock) {
        return historialStockMapper.toDto(historialStock);
    }

    @Override
    public HistorialStock convertToEntity(HistorialStockDTO historialStockDTO) {
        return historialStockMapper.toEntity(historialStockDTO);
    }

    public HistorialStockDTO createHistorialStock(HistorialStockDTO historialStockDTO) {
        ValidatorUtils.validateEntity(historialStockDTO, validator);

        HistorialStock historialStock = historialStockMapper.toEntity(historialStockDTO);

        try {
            HistorialStock savedHistorialStock = historialStockRepository.save(historialStock);
            return historialStockMapper.toDto(savedHistorialStock);
        } catch (PersistenceException e) {
            throw new DataAccessException("Error guardando HistorialStock", e);
        }
    }

    @Transactional
    public HistorialStockDTO updateHistorialStock(Long id, HistorialStockDTO historialStockDTO) {
        ValidatorUtils.validateEntity(historialStockDTO, validator);
        try {
            logger.info("Actualizando HistorialStock con id: {}", id);
            HistorialStock existingHistorialStock = historialStockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HistorialStock no encontrado con el id: " + id));

            // Actualizar los campos de la entidad existente con los valores del DTO
            existingHistorialStock.setCantidadAnterior(historialStockDTO.getCantidadAnterior());
            existingHistorialStock.setCantidadNueva(historialStockDTO.getCantidadNueva());
            existingHistorialStock.setFecha(historialStockDTO.getFechaActualizacion());
            existingHistorialStock.setMotivo(historialStockDTO.getMotivo());
            existingHistorialStock.setTipoMovimiento(historialStockDTO.getTipoMovimiento());
            existingHistorialStock.setObservacion(historialStockDTO.getObservacion());

            // Manejar relaciones
            existingHistorialStock.setProducto(productoRepository.findById(historialStockDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + historialStockDTO.getProductoId())));
            existingHistorialStock.setDeposito(depositoRepository.findById(historialStockDTO.getDepositoId()).orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + historialStockDTO.getDepositoId())));
            existingHistorialStock.setUsuario(usuarioRepository.findById(historialStockDTO.getUsuarioId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + historialStockDTO.getUsuarioId())));
            existingHistorialStock.setStock(stockRepository.findById(historialStockDTO.getStockId()).orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado con el id: " + historialStockDTO.getStockId())));

            HistorialStock updatedHistorialStock = historialStockRepository.save(existingHistorialStock);
            logger.info("HistorialStock actualizado exitosamente con id: {}", id);
            return convertToDto(updatedHistorialStock);
        } catch (ResourceNotFoundException e) {
            throw e;
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
            HistorialStock historialStock = historialStockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HistorialStock no encontrado"));
            return convertToDto(historialStock);
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
            return historialStockPage.map(historialStockMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los HistorialStock con paginación", e);
            throw new DataAccessException("Error obteniendo todos los HistorialStock con paginación", e);
        }
    }

}