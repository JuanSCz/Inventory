package com.interonda.Inventory.service.impl;

import com.interonda.Inventory.entity.Compra;
import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.entity.Proveedor;
import com.interonda.Inventory.entityDTO.CompraDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.CompraMapper;
import com.interonda.Inventory.repository.CompraRepository;
import com.interonda.Inventory.repository.ProveedorRepository;
import com.interonda.Inventory.service.CompraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final CompraMapper compraMapper;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, ProveedorRepository proveedorRepository, CompraMapper compraMapper) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.compraMapper = compraMapper;
    }

    @Override
    public CompraDTO convertToDto(Compra compra) {
        return compraMapper.toDto(compra);
    }

    @Override
    public Compra convertToEntity(CompraDTO compraDTO) {
        Compra compra = compraMapper.toEntity(compraDTO);
        Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el id: " + compraDTO.getProveedorId()));
        compra.setProveedor(proveedor);
        return compra;
    }

    @Override
    @Transactional
    public CompraDTO createCompra(CompraDTO compraDTO) {
        try {
            logger.info("Creando nueva Compra");
            Compra compra = convertToEntity(compraDTO);
            Compra savedCompra = compraRepository.save(compra);
            logger.info("Compra creada exitosamente con id: {}", savedCompra.getId());
            return convertToDto(savedCompra);
        } catch (Exception e) {
            logger.error("Error creando Compra", e);
            throw new DataAccessException("Error creando Compra", e);
        }
    }

    @Override
    @Transactional
    public CompraDTO updateCompra(Long id, CompraDTO compraDTO) {
        try {
            logger.info("Actualizando Compra con id: {}", id);
            Compra compra = compraRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + id));
            compra.setFecha(compraDTO.getFecha());
            compra.setTotal(compraDTO.getTotal());
            compra.setMetodoPago(compraDTO.getMetodoPago());
            compra.setEstado(compraDTO.getEstado());
            compra.setImpuestos(compraDTO.getImpuestos());
            Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con el id: " + compraDTO.getProveedorId()));
            compra.setProveedor(proveedor);
            List<DetalleCompra> detallesCompra = compraDTO.getDetallesCompra().stream()
                    .map(compraMapper::toDetalleCompraEntity)
                    .collect(Collectors.toList());
            compra.setDetallesCompra(detallesCompra);
            Compra updatedCompra = compraRepository.save(compra);
            logger.info("Compra actualizada exitosamente con id: {}", updatedCompra.getId());
            return convertToDto(updatedCompra);
        } catch (ResourceNotFoundException e) {
            logger.warn("Compra no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error actualizando Compra por id: " + id, e);
            throw new DataAccessException("Error actualizando Compra por id: " + id, e);
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
            logger.error("Error eliminando Compra por id: " + id, e);
            throw new DataAccessException("Error eliminando Compra por id: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraDTO> getAllCompras() {
        try {
            logger.info("Obteniendo todas las Compras");
            List<Compra> compras = compraRepository.findAll();
            return compras.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error obteniendo todas las Compras", e);
            throw new DataAccessException("Error obteniendo todas las Compras", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompraDTO getCompraById(Long id) {
        try {
            logger.info("Obteniendo Compra con id: {}", id);
            Compra compra = compraRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con el id: " + id));
            return convertToDto(compra);
        } catch (ResourceNotFoundException e) {
            logger.warn("Compra no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error obteniendo Compra por id: " + id, e);
            throw new DataAccessException("Error obteniendo Compra por id: " + id, e);
        }
    }
}