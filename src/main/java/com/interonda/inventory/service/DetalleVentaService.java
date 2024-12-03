package com.interonda.inventory.service;

import com.interonda.inventory.entity.DetalleVenta;
import com.interonda.inventory.dto.DetalleVentaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DetalleVentaService {

    DetalleVentaDTO convertToDto(DetalleVenta DetalleVenta);

    DetalleVenta convertToEntity(DetalleVentaDTO DetalleVentaDTO);

    DetalleVentaDTO createDetalleVenta(DetalleVentaDTO DetalleVentaDTO);

    DetalleVentaDTO updateDetalleVenta(Long id, DetalleVentaDTO detalleVentaDTO);

    void deleteDetalleVenta(Long id);

    Page<DetalleVentaDTO> getAllDetalleVentas(Pageable pageable);

    DetalleVentaDTO getDetalleVentaById(Long id);
}