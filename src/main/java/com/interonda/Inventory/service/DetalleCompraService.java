package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.entityDTO.DetalleCompraDTO;

import java.util.List;

public interface DetalleCompraService {

    DetalleCompraDTO convertToDto(DetalleCompra detalleCompra);

    DetalleCompra convertToEntity(DetalleCompraDTO detalleCompraDTO);

    DetalleCompraDTO createDetalleCompra(DetalleCompraDTO detalleCompraDTO);

    DetalleCompraDTO updateDetalleCompra(Long id, DetalleCompraDTO detalleCompraDTO);

    void deleteDetalleCompra(Long id);

    List<DetalleCompraDTO> getAllDetalleCompra();

    DetalleCompraDTO getDetalleCompraById(Long id);
}
