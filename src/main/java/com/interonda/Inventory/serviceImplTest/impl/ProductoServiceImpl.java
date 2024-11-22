package com.interonda.Inventory.serviceImplTest.impl;

import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.dto.ProductoDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.ProductoMapper;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.serviceImplTest.ProductoService;

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
public class ProductoServiceImpl implements ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final Validator validator;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper, Validator validator) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.validator = validator;
    }

    @Override
    public ProductoDTO convertToDto(Producto producto) {
        return productoMapper.toDto(producto);
    }

    @Override
    public Producto convertToEntity(ProductoDTO productoDTO) {
        return productoMapper.toEntity(productoDTO);
    }

    @Override
    @Transactional
    public ProductoDTO createProducto(ProductoDTO productoDTO) {
        ValidatorUtils.validateEntity(productoDTO, validator);
        if (productoDTO == null) {
            throw new IllegalArgumentException("ProductoDTO no puede ser null");
        }
        try {
            logger.info("Creando nuevo Producto");
            Producto producto = productoMapper.toEntity(productoDTO);
            Producto savedProducto = productoRepository.save(producto);
            logger.info("Producto creado exitosamente con id: {}", savedProducto.getId());
            return productoMapper.toDto(savedProducto);
        } catch (Exception e) {
            logger.error("Error guardando Producto", e);
            throw new DataAccessException("Error guardando Producto", e);
        }
    }

    @Override
    @Transactional
    public ProductoDTO updateProducto(ProductoDTO productoDTO) {
        ValidatorUtils.validateEntity(productoDTO, validator);
        try {
            logger.info("Actualizando Producto con id: {}", productoDTO.getId());
            Producto producto = productoRepository.findById(productoDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + productoDTO.getId()));
            producto = productoMapper.toEntity(productoDTO);
            Producto updatedProducto = productoRepository.save(producto);
            logger.info("Producto actualizado exitosamente con id: {}", updatedProducto.getId());
            return productoMapper.toDto(updatedProducto);
        } catch (ResourceNotFoundException e) {
            logger.warn("Producto no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Producto", e);
            throw new DataAccessException("Error actualizando Producto", e);
        }
    }

    @Override
    @Transactional
    public void deleteProducto(Long id) {
        try {
            logger.info("Eliminando Producto con id: {}", id);
            if (!productoRepository.existsById(id)) {
                throw new ResourceNotFoundException("Producto no encontrado con el id: " + id);
            }
            productoRepository.deleteById(id);
            logger.info("Producto eliminado exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Producto no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Producto", e);
            throw new DataAccessException("Error eliminando Producto", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO getProducto(Long id) {
        try {
            logger.info("Obteniendo Producto con id: {}", id);
            Producto producto = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + id));
            return productoMapper.toDto(producto);
        } catch (ResourceNotFoundException e) {
            logger.warn("Producto no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Producto", e);
            throw new DataAccessException("Error obteniendo Producto", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> getAllProductos(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los Productos con paginación");
            Page<Producto> productos = productoRepository.findAll(pageable);
            return productos.map(productoMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los Productos con paginación", e);
            throw new DataAccessException("Error obteniendo todos los Productos con paginación", e);
        }
    }
}