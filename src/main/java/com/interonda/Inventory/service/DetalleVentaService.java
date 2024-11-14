package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.DetalleVenta;
import com.interonda.Inventory.entityDTO.DetalleVentaDTO;

import java.util.List;

public interface DetalleVentaService {

    DetalleVentaDTO convertToDto(DetalleVenta DetalleVenta);

    DetalleVenta convertToEntity(DetalleVentaDTO DetalleVentaDTO);

    DetalleVentaDTO createDetalleVenta(DetalleVentaDTO DetalleVentaDTO);

    DetalleVentaDTO updateDetalleVenta(Long id, DetalleVentaDTO detalleVentaDTO);

    void deleteDetalleVenta(Long id);

    List<DetalleVentaDTO> getAllDetalleVentas();

    DetalleVentaDTO getDetalleVentaById(Long id);

}