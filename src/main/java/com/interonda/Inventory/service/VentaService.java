package com.interonda.Inventory.service;

import com.interonda.Inventory.entity.Venta;
import com.interonda.Inventory.entityDTO.VentaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VentaService {

    VentaDTO convertToDto(Venta venta);

    Venta convertToEntity(VentaDTO ventaDTO);

    VentaDTO createVenta(VentaDTO ventaDTO);

    VentaDTO updateVenta(VentaDTO ventaDTO);

    void deleteVenta(Long id);

    VentaDTO getVenta(Long id);

    Page<VentaDTO> getAllVentas(Pageable pageable);


}
