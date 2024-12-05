package com.interonda.inventory.service.impl;

import com.interonda.inventory.entity.Proveedor;
import com.interonda.inventory.dto.ProveedorDTO;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.ProveedorMapper;
import com.interonda.inventory.repository.ProveedorRepository;
import com.interonda.inventory.service.ProveedorService;

import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorServiceImpl.class);

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;
    private final Validator validator;

    @Autowired
    public ProveedorServiceImpl(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper, Validator validator) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
        this.validator = validator;
    }

    @Override
    public ProveedorDTO convertToDto(Proveedor proveedor) {
        return proveedorMapper.toDto(proveedor);
    }

    @Override
    public Proveedor convertToEntity(ProveedorDTO proveedorDTO) {
        return proveedorMapper.toEntity(proveedorDTO);
    }

    @Override
    @Transactional
    public ProveedorDTO createProveedor(ProveedorDTO proveedorDTO) {
        ValidatorUtils.validateEntity(proveedorDTO, validator);
        try {
            logger.info("Creando nuevo Proveedor");
            Proveedor proveedor = proveedorMapper.toEntity(proveedorDTO);
            Proveedor savedProveedor = proveedorRepository.save(proveedor);
            logger.info("Proveedor creado exitosamente con id: {}", savedProveedor.getId());
            return proveedorMapper.toDto(savedProveedor);
        } catch (Exception e) {
            logger.error("Error guardando Proveedor", e);
            throw new DataAccessException("Error guardando Proveedor", e);
        }
    }

    @Override
    @Transactional
    public ProveedorDTO updateProveedor(ProveedorDTO proveedorDTO) {
        ValidatorUtils.validateEntity(proveedorDTO, validator);
        try {
            logger.info("Actualizando Proveedor con id: {}", proveedorDTO.getId());
            Proveedor proveedor = proveedorRepository.findById(proveedorDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el id: " + proveedorDTO.getId()));
            proveedor = proveedorMapper.toEntity(proveedorDTO);
            Proveedor updatedProveedor = proveedorRepository.save(proveedor);
            logger.info("Proveedor actualizado exitosamente con id: {}", updatedProveedor.getId());
            return proveedorMapper.toDto(updatedProveedor);
        } catch (ResourceNotFoundException e) {
            logger.warn("Proveedor no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Proveedor", e);
            throw new DataAccessException("Error actualizando Proveedor", e);
        }
    }

    @Override
    @Transactional
    public void deleteProveedor(Long id) {
        try {
            logger.info("Eliminando Proveedor con id: {}", id);
            if (!proveedorRepository.existsById(id)) {
                throw new ResourceNotFoundException("Proveedor no encontrado con el id: " + id);
            }
            proveedorRepository.deleteById(id);
            logger.info("Proveedor eliminado exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Proveedor no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Proveedor", e);
            throw new DataAccessException("Error eliminando Proveedor", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDTO getProveedor(Long id) {
        try {
            logger.info("Obteniendo Proveedor con id: {}", id);
            Proveedor proveedor = proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el id: " + id));
            return proveedorMapper.toDto(proveedor);
        } catch (ResourceNotFoundException e) {
            logger.warn("Proveedor no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Proveedor", e);
            throw new DataAccessException("Error obteniendo Proveedor", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProveedorDTO> getAllProveedores(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Proveedores con paginación");
            Page<Proveedor> proveedores = proveedorRepository.findAll(pageable);
            return proveedores.map(proveedorMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Proveedores con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Proveedores con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProveedorDTO> searchProveedoresByName(String nombre, Pageable pageable) {
        try {
            logger.info("Buscando Proveedores por nombre: {}", nombre);
            Page<Proveedor> proveedores = proveedorRepository.findByNombreContainingIgnoreCase(nombre, pageable);
            return proveedores.map(proveedorMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Proveedores por nombre", e);
            throw new DataAccessException("Error buscando Proveedores por nombre", e);
        }
    }
}

