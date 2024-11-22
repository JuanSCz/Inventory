package com.interonda.Inventory.serviceImplTest.impl;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.dto.DetalleCompraDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.DetalleCompraMapper;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.repository.DetalleCompraRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.serviceImplTest.DetalleCompraService;

import com.interonda.Inventory.utils.ValidatorUtils;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DetalleCompraServiceImpl implements DetalleCompraService {

    private static final Logger logger = LoggerFactory.getLogger(DetalleCompraServiceImpl.class);

    private final DetalleCompraRepository detalleCompraRepository;
    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final DetalleCompraMapper detalleCompraMapper;
    private final Validator validator;

    @Autowired
    public DetalleCompraServiceImpl(DetalleCompraRepository detalleCompraRepository, CompraRepository compraRepository, ProductoRepository productoRepository, DetalleCompraMapper detalleCompraMapper, Validator validator) {
        this.detalleCompraRepository = detalleCompraRepository;
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.detalleCompraMapper = detalleCompraMapper;
        this.validator = validator;
    }

    @Override
    public DetalleCompraDTO convertToDto(DetalleCompra detalleCompra) {
        return detalleCompraMapper.toDto(detalleCompra);
    }

    @Override
    public DetalleCompra convertToEntity(DetalleCompraDTO detalleCompraDTO) {
        return detalleCompraMapper.toEntity(detalleCompraDTO);
    }

    @Override
    @Transactional
    public DetalleCompraDTO createDetalleCompra(DetalleCompraDTO detalleCompraDTO) {
        ValidatorUtils.validateEntity(detalleCompraDTO, validator);

        Compra compra = compraRepository.findById(detalleCompraDTO.getCompraId()).orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + detalleCompraDTO.getCompraId()));

        Producto producto = productoRepository.findById(detalleCompraDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleCompraDTO.getProductoId()));

        DetalleCompra detalleCompra = detalleCompraMapper.toEntity(detalleCompraDTO);
        detalleCompra.setCompra(compra);
        detalleCompra.setProducto(producto);

        try {
            DetalleCompra savedDetalleCompra = detalleCompraRepository.save(detalleCompra);
            return detalleCompraMapper.toDto(savedDetalleCompra);
        } catch (PersistenceException e) {
            throw new DataAccessException("Error creando DetalleCompra", e);
        }
    }

    @Override
    @Transactional
    public DetalleCompraDTO updateDetalleCompra(Long id, DetalleCompraDTO detalleCompraDTO) {
        ValidatorUtils.validateEntity(detalleCompraDTO, validator);
        try {
            logger.info("Actualizando DetalleCompra con id: {}", id);
            DetalleCompra detalleCompra = detalleCompraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("DetalleCompra no encontrada con el id: " + id));
            detalleCompra.setCantidad(detalleCompraDTO.getCantidad());
            detalleCompra.setPrecioUnitario(detalleCompraDTO.getPrecioUnitario());

            Compra compra = compraRepository.findById(detalleCompraDTO.getCompraId()).orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + detalleCompraDTO.getCompraId()));
            detalleCompra.setCompra(compra);

            Producto producto = productoRepository.findById(detalleCompraDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleCompraDTO.getProductoId()));
            detalleCompra.setProducto(producto);

            DetalleCompra updatedDetalleCompra = detalleCompraRepository.save(detalleCompra);
            logger.info("DetalleCompra actualizado exitosamente con id: {}", updatedDetalleCompra.getId());
            return convertToDto(updatedDetalleCompra);
        } catch (ResourceNotFoundException e) {
            logger.warn("DetalleCompra no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando DetalleCompra por id: " + id, e);
            throw new DataAccessException("Error actualizando DetalleCompra por id: " + id, e);
        }
    }

    @Override
    @Transactional
    public void deleteDetalleCompra(Long id) {
        try {
            logger.info("Eliminando DetalleCompra con id: {}", id);
            if (!detalleCompraRepository.existsById(id)) {
                throw new ResourceNotFoundException("DetalleCompra no encontrada con el id: " + id);
            }
            detalleCompraRepository.deleteById(id);
            logger.info("DetalleCompra eliminado exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("DetalleCompra no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando DetalleCompra por id: " + id, e);
            throw new DataAccessException("Error eliminando DetalleCompra por id: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetalleCompraDTO> getAllDetalleCompra(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los DetalleCompra con paginación");
            Page<DetalleCompra> detalleCompraPage = detalleCompraRepository.findAll(pageable);
            return detalleCompraPage.map(detalleCompraMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los DetalleCompra con paginación", e);
            throw new DataAccessException("Error obteniendo todos los DetalleCompra con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleCompraDTO getDetalleCompraById(Long id) {
        try {
            logger.info("Obteniendo DetalleCompra con id: {}", id);
            DetalleCompra detalleCompra = detalleCompraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Detalle de compra no encontrado con el id: " + id));
            return convertToDto(detalleCompra);
        } catch (ResourceNotFoundException e) {
            logger.warn("DetalleCompra no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo DetalleCompra por id: " + id, e);
            throw new DataAccessException("Error obteniendo DetalleCompra por id: " + id, e);
        }
    }
}
