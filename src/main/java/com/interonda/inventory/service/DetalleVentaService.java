package com.interonda.inventory.service;

import com.interonda.inventory.dto.DetalleCompraDTO;
import com.interonda.inventory.entity.DetalleCompra;
import com.interonda.inventory.entity.DetalleVenta;
import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.entity.Producto;
import com.interonda.inventory.entity.Venta;
import com.interonda.inventory.exceptions.DataAccessException;
import com.interonda.inventory.exceptions.ResourceNotFoundException;
import com.interonda.inventory.mapper.DetalleVentaMapper;
import com.interonda.inventory.repository.DetalleVentaRepository;
import com.interonda.inventory.repository.ProductoRepository;
import com.interonda.inventory.repository.VentaRepository;
import com.interonda.inventory.utils.ValidatorUtils;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface DetalleVentaService {

    DetalleVentaDTO convertToDto(DetalleVenta detalleVenta);

    DetalleVenta convertToEntity(DetalleVentaDTO detalleVentaDTO);

    DetalleVentaDTO createDetalleVenta(DetalleVentaDTO detalleVentaDTO);

    DetalleVentaDTO updateDetalleVenta(Long id, DetalleVentaDTO detalleVentaDTO);

    void deleteDetalleVenta(Long id);

    Page<DetalleVentaDTO> getAllDetalleVenta(Pageable pageable);

    DetalleVentaDTO getDetalleVentaById(Long id);

    List<DetalleVentaDTO> getDetallesByVentaId(Long ventaId);

}