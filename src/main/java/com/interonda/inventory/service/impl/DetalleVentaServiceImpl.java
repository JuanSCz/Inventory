package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.entity.DetalleVenta;
import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.entity.Venta;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.DetalleVentaMapper;
import com.interonda.inventory.repository.DepositoRepository;
import com.interonda.inventory.repository.DetalleVentaRepository;
import com.interonda.inventory.repository.ProductoRepository;
import com.interonda.inventory.repository.VentaRepository;
import com.interonda.inventory.service.DetalleVentaService;
import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {
    private static final Logger logger = LoggerFactory.getLogger(DetalleVentaServiceImpl.class);

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaMapper detalleVentaMapper;
    private final Validator validator;
    private final DepositoRepository depositoRepository;

    @Autowired
    public DetalleVentaServiceImpl(DetalleVentaRepository detalleVentaRepository, VentaRepository ventaRepository, ProductoRepository productoRepository, DetalleVentaMapper detalleVentaMapper, Validator validator, DepositoRepository depositoRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.detalleVentaMapper = detalleVentaMapper;
        this.validator = validator;
        this.depositoRepository = depositoRepository;
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
        ValidatorUtils.validateEntity(detalleVentaDTO, validator);

        Venta venta = ventaRepository.findById(detalleVentaDTO.getVentaId()).orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el id: " + detalleVentaDTO.getVentaId()));

        Producto producto = productoRepository.findById(detalleVentaDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleVentaDTO.getProductoId()));

        DetalleVenta detalleVenta = detalleVentaMapper.toEntity(detalleVentaDTO);
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        detalleVenta.setDeposito(depositoRepository.findById(detalleVentaDTO.getDepositoId()).orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + detalleVentaDTO.getDepositoId())));

        try {
            DetalleVenta savedDetalleVenta = detalleVentaRepository.save(detalleVenta);
            return detalleVentaMapper.toDto(savedDetalleVenta);
        } catch (PersistenceException e) {
            throw new DataAccessException("Error creando DetalleVenta", e);
        }
    }

    @Override
    @Transactional
    public DetalleVentaDTO updateDetalleVenta(Long id, DetalleVentaDTO detalleVentaDTO) {
        ValidatorUtils.validateEntity(detalleVentaDTO, validator);
        try {
            logger.info("Actualizando DetalleVenta con id: {}", id);
            DetalleVenta detalleVenta = detalleVentaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("DetalleVenta no encontrada con el id: " + id));
            detalleVenta.setCantidad(detalleVentaDTO.getCantidad());
            detalleVenta.setPrecioUnitario(detalleVentaDTO.getPrecioUnitario());

            Venta venta = ventaRepository.findById(detalleVentaDTO.getVentaId()).orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el id: " + detalleVentaDTO.getVentaId()));
            detalleVenta.setVenta(venta);

            Producto producto = productoRepository.findById(detalleVentaDTO.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleVentaDTO.getProductoId()));
            detalleVenta.setProducto(producto);

            detalleVenta.setDeposito(depositoRepository.findById(detalleVentaDTO.getDepositoId()).orElseThrow(() -> new ResourceNotFoundException("Depósito no encontrado con el id: " + detalleVentaDTO.getDepositoId())));

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
                throw new ResourceNotFoundException("DetalleVenta no encontrada con el id: " + id);
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
    public Page<DetalleVentaDTO> getAllDetalleVenta(Pageable pageable) {
        try {
            logger.info("Obteniendo todos los DetalleVenta con paginación");
            Page<DetalleVenta> detalleVentaPage = detalleVentaRepository.findAll(pageable);
            return detalleVentaPage.map(detalleVentaMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todos los DetalleVenta con paginación", e);
            throw new DataAccessException("Error obteniendo todos los DetalleVenta con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleVentaDTO getDetalleVentaById(Long id) {
        try {
            logger.info("Obteniendo DetalleVenta con id: {}", id);
            DetalleVenta detalleVenta = detalleVentaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Detalle de venta no encontrado con el id: " + id));
            return convertToDto(detalleVenta);
        } catch (ResourceNotFoundException e) {
            logger.warn("DetalleVenta no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo DetalleVenta por id: " + id, e);
            throw new DataAccessException("Error obteniendo DetalleVenta por id: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaDTO> getDetallesByVentaId(Long ventaId) {
        try {
            logger.info("Obteniendo detalles de venta para la venta con id: {}", ventaId);
            List<DetalleVenta> detallesVenta = detalleVentaRepository.findByVentaId(ventaId);
            return detallesVenta.stream().map(detalle -> {
                DetalleVentaDTO detalleDTO = detalleVentaMapper.toDto(detalle);
                detalleDTO.setProductoId(detalle.getProducto().getId());
                detalleDTO.setProductoNombre(detalle.getProducto().getNombre());
                detalleDTO.setDepositoNombre(detalle.getDeposito().getNombre());
                detalleDTO.setDepositoId(detalle.getDeposito().getId());
                return detalleDTO;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error obteniendo detalles de venta para la venta con id: " + ventaId, e);
            throw new DataAccessException("Error obteniendo detalles de venta para la venta con id: " + ventaId, e);
        }
    }
}
