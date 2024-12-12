package com.interonda.inventory.service.impl;

import com.interonda.inventory.dto.CompraDTO;
import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.entity.Compra;
import com.interonda.inventory.entity.DetalleCompra;
import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.entity.Proveedor;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.CompraMapper;
import com.interonda.inventory.repository.CompraRepository;
import com.interonda.inventory.repository.DetalleCompraRepository;
import com.interonda.inventory.repository.ProductoRepository;
import com.interonda.inventory.repository.ProveedorRepository;
import com.interonda.inventory.service.CompraService;
import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {
    private static final Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    @Autowired
    private final CompraMapper compraMapper;

    private final Validator validator;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, ProveedorRepository proveedorRepository, ProductoRepository productoRepository, DetalleCompraRepository detalleCompraRepository, CompraMapper compraMapper, Validator validator) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.compraMapper = compraMapper;
        this.validator = validator;
    }

    @Override
    public CompraDTO convertToDto(Compra compra) {
        return compraMapper.toDto(compra);
    }

    @Override
    public Compra convertToEntity(CompraDTO compraDTO) {
        return compraMapper.toEntity(compraDTO);
    }

    @Override
    @Transactional
    public CompraDTO createCompra(CompraDTO compraDTO) {
        ValidatorUtils.validateEntity(compraDTO, validator);
        try {
            Compra compra = compraMapper.toEntity(compraDTO);
            Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el id: " + compraDTO.getProveedorId()));
            compra.setProveedor(proveedor);

            // Manejar los detalles de la compra
            List<DetalleCompra> detallesCompra = compraDTO.getDetallesCompra().stream()
                    .map(detalleDTO -> {
                        DetalleCompra detalle = new DetalleCompra();
                        detalle.setCantidad(detalleDTO.getCantidad());
                        detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                        Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleDTO.getProductoId()));
                        detalle.setProducto(producto);
                        detalle.setCompra(compra);
                        return detalle;
                    }).collect(Collectors.toList());
            compra.setDetallesCompra(detallesCompra);

            Compra savedCompra = compraRepository.save(compra);
            return compraMapper.toDto(savedCompra);
        } catch (Exception e) {
            throw new DataAccessException("Error guardando Compra", e);
        }
    }

    @Override
    @Transactional
    public CompraDTO updateCompra(CompraDTO compraDTO) {
        ValidatorUtils.validateEntity(compraDTO, validator);
        try {
            logger.info("Actualizando Compra con id: {}", compraDTO.getId());
            Compra compra = compraRepository.findById(compraDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + compraDTO.getId()));

            // Actualizar los campos de la compra
            compra.setFecha(compraDTO.getFecha());
            compra.setTotal(compraDTO.getTotal());
            compra.setMetodoPago(compraDTO.getMetodoPago());
            compra.setEstado(compraDTO.getEstado());
            compra.setImpuestos(compraDTO.getImpuestos());

            // Manejar la relación con Proveedor manualmente
            Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el id: " + compraDTO.getProveedorId()));
            compra.setProveedor(proveedor);

            Compra updatedCompra = compraRepository.save(compra);
            logger.info("Compra actualizada exitosamente con id: {}", updatedCompra.getId());
            return compraMapper.toDto(updatedCompra);
        } catch (ResourceNotFoundException e) {
            logger.warn("Compra no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Compra", e);
            throw new DataAccessException("Error actualizando Compra", e);
        }
    }

    @Override
    @Transactional
    public void deleteCompra(Long id) {
        try {
            logger.info("Eliminando Compra con id: {}", id);
            if (!compraRepository.existsById(id)) {
                throw new ResourceNotFoundException("Compra no encontrada con el id: " + id);
            }
            compraRepository.deleteById(id);
            logger.info("Compra eliminada exitosamente con id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.warn("Compra no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error eliminando Compra", e);
            throw new DataAccessException("Error eliminando Compra", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompraDTO getCompra(Long id) {
        try {
            logger.info("Obteniendo Compra con id: {}", id);
            Compra compra = compraRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + id));
            return compraMapper.toDto(compra);
        } catch (ResourceNotFoundException e) {
            logger.warn("Compra no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Compra", e);
            throw new DataAccessException("Error obteniendo Compra", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompraDTO> getAllCompras(Pageable pageable) {
        try {
            logger.info("Obteniendo todas las Compras con paginación");
            Page<Compra> compras = compraRepository.findAll(pageable);
            return compras.map(compraMapper::toDto);
        } catch (Exception e) {
            logger.error("Error obteniendo todas las Compras con paginación", e);
            throw new DataAccessException("Error obteniendo todas las Compras con paginación", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompraDTO> searchComprasByFecha(LocalDate fecha, Pageable pageable) {
        try {
            logger.info("Buscando Compras por fecha: {}", fecha);
            Page<Compra> compras = compraRepository.findByFecha(fecha, pageable);
            return compras.map(compraMapper::toDto);
        } catch (Exception e) {
            logger.error("Error buscando Compras por fecha", e);
            throw new DataAccessException("Error buscando Compras por fecha", e);
        }
    }

    @Override
    @Transactional
    public DetalleCompraDTO addDetalleCompra(DetalleCompraDTO detalleCompraDTO) {
        ValidatorUtils.validateEntity(detalleCompraDTO, validator);
        try {
            DetalleCompra detalleCompra = new DetalleCompra();
            detalleCompra.setCantidad(detalleCompraDTO.getCantidad());
            detalleCompra.setPrecioUnitario(detalleCompraDTO.getPrecioUnitario());

            Compra compra = compraRepository.findById(detalleCompraDTO.getCompraId())
                    .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + detalleCompraDTO.getCompraId()));
            Producto producto = productoRepository.findById(detalleCompraDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el id: " + detalleCompraDTO.getProductoId()));

            detalleCompra.setCompra(compra);
            detalleCompra.setProducto(producto);

            DetalleCompra savedDetalle = detalleCompraRepository.save(detalleCompra);
            return compraMapper.toDetalleCompraDto(savedDetalle);
        } catch (Exception e) {
            throw new DataAccessException("Error guardando DetalleCompra", e);
        }
    }
}