package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.DetalleVenta;
import com.interonda.Inventory.entityDTO.DetalleVentaDTO;
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