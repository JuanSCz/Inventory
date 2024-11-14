package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.DetalleVenta;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.entityDTO.DetalleVentaDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.DetalleVentaMapper;
import com.interonda.Inventory.repository.DetalleVentaRepository;
import com.interonda.Inventory.repository.ProductoRepository;
import com.interonda.Inventory.repository.VentaRepository;
import com.interonda.Inventory.service.DetalleVentaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private static final Logger logger = LoggerFactory.getLogger(DetalleVentaServiceImpl.class);

    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final DetalleVentaMapper detalleVentaMapper;

    @Autowired
    public DetalleVentaServiceImpl(DetalleVentaRepository detalleVentaRepository, ProductoRepository productoRepository, VentaRepository ventaRepository, DetalleVentaMapper detalleVentaMapper) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
        this.detalleVentaMapper = detalleVentaMapper;
    }

    @Override
    public DetalleVentaDTO convertToDto(DetalleVenta detalleVenta) {
        return detalleVentaMapper.toDto(detalleVenta);
    }

    @Override
    public DetalleVenta convertToEntity(DetalleVentaDTO detalleVentaDTO) {
        return detalleVentaMapper.toEntity(detalleVentaDTO);
    }

    @Override
    @Transactional
    public DetalleVentaDTO createDetalleVenta(DetalleVentaDTO detalleVentaDTO) {
        try {
            logger.info("Creando nuevo DetalleVenta");
            DetalleVenta detalleVenta = convertToEntity(detalleVentaDTO);
            DetalleVenta savedDetalleVenta = detalleVentaRepository.save(detalleVenta);
            logger.info("DetalleVenta creado exitosamente con id: {}", savedDetalleVenta.getId());
            return convertToDto(savedDetalleVenta);
        } catch (Exception e) {
            logger.error("Error creando DetalleVenta", e);
            throw new DataAccessException("Error creando DetalleVenta", e);
        }
    }

    @Override
    @Transactional
    public DetalleVentaDTO updateDetalleVenta(Long id, DetalleVentaDTO detalleVentaDTO) {
        try {
            logger.info("Actualizando DetalleVenta con id: {}", id);
            DetalleVenta detalleVenta = detalleVentaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("DetalleVenta no encontrado con el id: " + id));
            detalleVenta.setCantidad(detalleVentaDTO.getCantidad());
            detalleVenta.setPrecioUnitario(detalleVentaDTO.getPrecioUnitario());

            Venta venta = ventaRepository.findById(detalleVentaDTO.getVentaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el id: " + detalleVentaDTO.getVentaId()));
            detalleVenta.setVenta(venta);

            Producto producto = productoRepository.findById(detalleVentaDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleVentaDTO.getProductoId()));
            detalleVenta.setProducto(producto);

            DetalleVenta updatedDetalleVenta = detalleVentaRepository.save(detalleVenta);
            logger.info("DetalleVenta actualizado exitosamente con id: {}", updatedDetalleVenta.getId());
            return convertToDto(updatedDetalleVenta);
        } catch (ResourceNotFoundException e) {
            logger.warn("DetalleVenta no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando DetalleVenta por id: " + id, e);
            throw new DataAccessException("Error actualizando DetalleVenta por id: " + id, e);
        }
    }

    @Override
    @Transactional
    public void deleteDetalleVenta(Long id) {
        try {
            logger.info("Eliminando DetalleVenta con id: {}", id);
            if (!detalleVentaRepository.existsById(id)) {
                throw new ResourceNotFoundException("DetalleVenta no encontrado con el id: " + id);
            }
            detalleVentaRepository.deleteById(id);
            logger.info("DetalleVenta eliminado exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("DetalleVenta no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando DetalleVenta por id: " + id, e);
            throw new DataAccessException("Error eliminando DetalleVenta por id: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaDTO> getAllDetalleVentas() {
        try {
            logger.info("Obteniendo todos los DetalleVenta");
            List<DetalleVenta> detalleVentas = detalleVentaRepository.findAll();
            return detalleVentas.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error obteniendo todos los DetalleVenta", e);
            throw new DataAccessException("Error obteniendo todos los DetalleVenta", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleVentaDTO getDetalleVentaById(Long id) {
        try {
            logger.info("Obteniendo DetalleVenta con id: {}", id);
            DetalleVenta detalleVenta = detalleVentaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("DetalleVenta no encontrado con el id: " + id));
            return convertToDto(detalleVenta);
        } catch (ResourceNotFoundException e) {
            logger.warn("DetalleVenta no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo DetalleVenta por id: " + id, e);
            throw new DataAccessException("Error obteniendo DetalleVenta por id: " + id, e);
        }
    }
}