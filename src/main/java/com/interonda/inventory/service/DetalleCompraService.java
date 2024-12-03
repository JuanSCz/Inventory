package com.interonda.inventory.service;

import com.interonda.inventory.entity.DetalleCompra;
import com.interonda.inventory.dto.DetalleCompraDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DetalleCompraService {

    DetalleCompraDTO convertToDto(DetalleCompra detalleCompra);

    DetalleCompra convertToEntity(DetalleCompraDTO detalleCompraDTO);

    DetalleCompraDTO createDetalleCompra(DetalleCompraDTO detalleCompraDTO);

    DetalleCompraDTO updateDetalleCompra(Long id, DetalleCompraDTO detalleCompraDTO);

    void deleteDetalleCompra(Long id);

    Page<DetalleCompraDTO> getAllDetalleCompra(Pageable pageable);

    DetalleCompraDTO getDetalleCompraById(Long id);
}
