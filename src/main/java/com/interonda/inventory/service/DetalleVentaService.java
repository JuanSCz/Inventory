package com.interonda.inventory.service;

import com.interonda.inventory.dto.DetalleVentaDTO;
import com.interonda.inventory.entity.DetalleVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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