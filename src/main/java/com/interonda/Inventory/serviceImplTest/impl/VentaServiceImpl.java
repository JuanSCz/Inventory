package com.interonda.Inventory.serviceImplTest.impl;

import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.dto.VentaDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.VentaMapper;
import com.interonda.Inventory.repository.VentaRepository;
import com.interonda.Inventory.serviceImplTest.VentaService;

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
public class VentaServiceImpl implements VentaService {
    private static final Logger logger = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;
    private final Validator validator;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository, VentaMapper ventaMapper, Validator validator) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
        this.validator = validator;
    }

    @Override
    public VentaDTO convertToDto(Venta venta) {
        return ventaMapper.toDto(venta);
    }

    @Override
    public Venta convertToEntity(VentaDTO ventaDTO) {
        return ventaMapper.toEntity(ventaDTO);
    }

    @Override
    @Transactional
    public VentaDTO createVenta(VentaDTO ventaDTO) {
        ValidatorUtils.validateEntity(ventaDTO, validator);
        try {
            logger.info("Creando nueva Venta");
            Venta venta = ventaMapper.toEntity(ventaDTO);
            Venta savedVenta = ventaRepository.save(venta);
            logger.info("Venta creada exitosamente con id: {}", savedVenta.getId());
            return ventaMapper.toDto(savedVenta);
        } catch (Exception e) {
            logger.error("Error guardando Venta", e);
            throw new DataAccessException("Error guardando Venta", e);
        }
    }

    @Override
    @Transactional
    public VentaDTO updateVenta(VentaDTO ventaDTO) {
        ValidatorUtils.validateEntity(ventaDTO, validator);
        try {
            logger.info("Actualizando Venta con id: {}", ventaDTO.getId());
            Venta venta = ventaRepository.findById(ventaDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el id: " + ventaDTO.getId()));
            venta = ventaMapper.toEntity(ventaDTO);
            Venta updatedVenta = ventaRepository.save(venta);
            logger.info("Venta actualizada exitosamente con id: {}", updatedVenta.getId());
            return ventaMapper.toDto(updatedVenta);
        } catch (ResourceNotFoundException e) {
            logger.warn("Venta no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Venta", e);
            throw new DataAccessException("Error actualizando Venta", e);
        }
    }

    @Override
    @Transactional
    public void deleteVenta(Long id) {
        try {
            logger.info("Eliminando Venta con id: {}", id);
            if (!ventaRepository.existsById(id)) {
                throw new ResourceNotFoundException("Venta no encontrada con el id: " + id);
            }
            ventaRepository.deleteById(id);
            logger.info("Venta eliminada exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Venta no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Venta", e);
            throw new DataAccessException("Error eliminando Venta", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO getVenta(Long id) {
        try {
            logger.info("Obteniendo Venta con id: {}", id);
            Venta venta = ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el id: " + id));
            return ventaMapper.toDto(venta);
        } catch (ResourceNotFoundException e) {
            logger.warn("Venta no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Venta", e);
            throw new DataAccessException("Error obteniendo Venta", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaDTO> getAllVentas(Pageable pageable) {
        try {
            logger.info("Obteniendo todas las Ventas con paginación");
            Page<Venta> ventas = ventaRepository.findAll(pageable);
            return ventas.map(ventaMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todas las Ventas con paginación", e);
            throw new DataAccessException("Error obteniendo todas las Ventas con paginación", e);
        }
    }
}
