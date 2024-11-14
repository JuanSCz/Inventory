package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entityDTO.DetalleCompraDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.DetalleCompraMapper;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.repository.DetalleCompraRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.service.DetalleCompraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetalleCompraServiceImpl implements DetalleCompraService {

    private static final Logger logger = LoggerFactory.getLogger(DetalleCompraServiceImpl.class);

    private final DetalleCompraRepository detalleCompraRepository;
    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final DetalleCompraMapper detalleCompraMapper;

    @Autowired
    public DetalleCompraServiceImpl(DetalleCompraRepository detalleCompraRepository, CompraRepository compraRepository, ProductoRepository productoRepository, DetalleCompraMapper detalleCompraMapper) {
        this.detalleCompraRepository = detalleCompraRepository;
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.detalleCompraMapper = detalleCompraMapper;
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
        try {
            logger.info("Creando nuevo DetalleCompra");
            DetalleCompra detalleCompra = convertToEntity(detalleCompraDTO);
            DetalleCompra savedDetalleCompra = detalleCompraRepository.save(detalleCompra);
            logger.info("DetalleCompra creado exitosamente con id: {}", savedDetalleCompra.getId());
            return convertToDto(savedDetalleCompra);
        } catch (Exception e) {
            logger.error("Error creando DetalleCompra", e);
            throw new DataAccessException("Error creando DetalleCompra", e);
        }
    }

    @Override
    @Transactional
    public DetalleCompraDTO updateDetalleCompra(Long id, DetalleCompraDTO detalleCompraDTO) {
        try {
            logger.info("Actualizando DetalleCompra con id: {}", id);
            DetalleCompra detalleCompra = detalleCompraRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra no encontrada con el id: " + id));
            detalleCompra.setCantidad(detalleCompraDTO.getCantidad());
            detalleCompra.setPrecioUnitario(detalleCompraDTO.getPrecioUnitario());

            Compra compra = compraRepository.findById(detalleCompraDTO.getCompraId())
                    .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + detalleCompraDTO.getCompraId()));
            detalleCompra.setCompra(compra);

            Producto producto = productoRepository.findById(detalleCompraDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleCompraDTO.getProductoId()));
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
    public List<DetalleCompraDTO> getAllDetalleCompra() {
        try {
            logger.info("Obteniendo todos los DetalleCompra");
            List<DetalleCompra> detalleCompraList = detalleCompraRepository.findAll();
            return detalleCompraList.stream().map(detalleCompraMapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error obteniendo todos los DetalleCompra", e);
            throw new DataAccessException("Error obteniendo todos los DetalleCompra", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleCompraDTO getDetalleCompraById(Long id) {
        try {
            logger.info("Obteniendo DetalleCompra con id: {}", id);
            DetalleCompra detalleCompra = detalleCompraRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Detalle de compra no encontrado con el id: " + id));
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
