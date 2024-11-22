package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.DetalleCompra;
import com.interonda.Inventory.dto.DetalleCompraDTO;
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
